package hu.bme.aut.freelancer_spring.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferServiceImp implements TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final TownRepository townRepository;
    private final VehicleRepository vehicleRepository;
    private final PackageRepository packageRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final DirectionService directionService;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Long save(TransferDto transferDto) {
        var carrier = userRepository.findById(transferDto.getCarrierId());
        var town = townRepository.findById(transferDto.getTownId());
        var vehicle = vehicleRepository.findById(transferDto.getVehicleId());


        if (carrier.isEmpty() || town.isEmpty() || vehicle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrier, town or vehicle not found");
        }
        if (!carrier.get().getId().equals(vehicle.get().getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Car ownerId ("+ vehicle.get().getOwner().getId() +
                            ") not matches carrier.id ("+ carrier.get().getId()+")");
        }
        var transfer = modelMapper.map(transferDto, Transfer.class);
        transfer.setCarrier(carrier.get());
        transfer.setVehicle(vehicle.get());
        transfer.setTown(town.get());
        transferRepository.save(transfer);
        storePackages(transfer, findPackages(transfer));
        return transfer.getId();
    }

    @Override
    public boolean delete(Long id) {
        var transfer = transferRepository.findById(id);
        if (transfer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found with id: " + id);
        }
        transferRepository.delete(transfer.get());
        return true;
    }

    @Override
    public List<Package> getPackages(Long id) {
        var transfer = transferRepository.findById(id);
        if (transfer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found with id: " + id);
        }
        return transfer.get().getPackages();
    }

    @Override
    public List<LatLng> calculateRoute(Long id) {
        var transferOptional = transferRepository.findById(id);
        var transfer = transferOptional.get();
        final var routes = directionService.getRouteForTransfer(transfer);
        transfer.setEncodedRoute(getEncodedRoute(routes));
        transferRepository.save(transfer);
        var time = setPickupTimeForPackages(routes.get(0), transfer.getPackages(), transfer.getStartTime());
        setArriveTimeForPackages(routes.get(1), transfer.getPackages(), time);
        transfer.getPackages().forEach(packageRepository::save);

        return PolylineEncoding.decode(transfer.getEncodedRoute());
    }

    private LocalTime setPickupTimeForPackages(DirectionsRoute route, List<Package> packages, LocalTime startTime) {
        int i = 0;
        LocalTime time = startTime;
        for (var leg : route.legs) {
            if (i != route.legs.length - 1) {
                time = time.plusSeconds(leg.duration.inSeconds);
                var pack = packages.get(route.waypointOrder[i]);
                pack.setPickupTime(time);
            }
            i++;
        }
        return time;
    }

    private void setArriveTimeForPackages(DirectionsRoute route, List<Package> packages, LocalTime startTime) {
        int i = 0;
        LocalTime time = startTime;
        for (var leg : route.legs) {
            if (i != route.legs.length - 1) {
                time = time.plusSeconds(leg.duration.inSeconds);
                var pack = packages.get(route.waypointOrder[i]);
                pack.setArriveTime(time);
            }
            i++;
        }
    }
    private String getEncodedRoute(List<DirectionsRoute> routes) {
        var decodedLatLngList = new ArrayList<LatLng>();
        int i = 0;
        for (var leg : routes.get(0).legs) {
            if (i != routes.get(0).legs.length - 1) {
                for (var step : leg.steps) {
                    decodedLatLngList.addAll(step.polyline.decodePath());
                }
            }
            i++;
        }
        decodedLatLngList.addAll(routes.get(1).overviewPolyline.decodePath());

        return PolylineEncoding.encode(decodedLatLngList);
    }
    private List<Package> findPackages(Transfer transfer) {
        return packageRepository.findByTransferIsNullAndTownAndDateLimitAfterOrderByCreatedAt(transfer.getTown(), transfer.getCreatedAt());
    }
    private void storePackages(Transfer transfer, List<Package> packages) {
        packages.forEach(pack -> {
            if (transfer.fitPackage(pack)) {
                transfer.addPackage(pack);
                pack.setTransfer(transfer);
                packageRepository.save(pack);
            }
        });
    }
}
