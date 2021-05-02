package hu.bme.aut.freelancer_spring.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.dto.NavigationDto;
import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

    @Transactional
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

    @Transactional
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
    public List<Transfer> getTransfersOnDate(Date date) {
        return transferRepository.findAllByDate(date);
    }

    /**
     * This is called by the scheduler to calculate the route for the transfer.
     * It also saves the estimated pick up and delivery times for the packages.
     * @param transfer Transfer to calculate the route for
     */
    @Transactional
    @Override
    public void calculateRoute(Transfer transfer) {
        if (transfer.getPackages().size() == 0)
            return;
        final var routes =
                directionService.getRouteForTransfer(transfer, new LatLng(transfer.getFromLat(), transfer.getFromLong()));
        transfer.setEncodedRoute(getEncodedRoute(routes));
        transferRepository.save(transfer);
        var time = setPickupTimeForPackages(routes.get(0), transfer.getPackages(), transfer.getStartTime());
        setArriveTimeForPackages(routes.get(1), transfer.getPackages(), time);
        transfer.getPackages().forEach(packageRepository::save);
    }

    /**
     * Calculated the optimal route for a transfer from a starting point
     * @param id ID of the transfer to calculate the route
     * @param origin The starting point from which to calculate
     * @return NavigationDto which holds a string with the and url that opens Google Maps and starts a navigation with the calcualted route
     */
    @Override
    public NavigationDto getNavigationUrl(Long id, LatLng origin) {
        var transfer = transferRepository.findById(id);
        if (transfer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found with id: " + id);
        }
        var routes = directionService.getRouteForTransfer(transfer.get(), origin);

        return new NavigationDto("https://www.google.com/maps/dir/?api=1" +
                "&origin=" + origin.lat + "," + origin.lng +
                "&destination=" + transfer.get().getToLat() + "," + transfer.get().getToLong() +
                "&waypoints=" + getWaypointsUrlPart(routes.get(0)) + getWaypointsUrlPart(routes.get(1))
        );
        //format:  &origin=47.0120,19.121212&destination=47.1120,19.421212&waypoints=47.4120,19.121212|47.6120,19.121212|
    }

    private String getWaypointsUrlPart(DirectionsRoute route) {
        int i = 0;
        StringBuilder waypointsUrl = new StringBuilder();
        for (var leg : route.legs) {
            if (i != route.legs.length - 1) {
                waypointsUrl.append(leg.endLocation.lat).append(",").append(leg.endLocation.lng).append("|");
            }
            i++;
        }

        return waypointsUrl.toString();
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

    /**
     * Routes have all the LatLng points in an encoded string. To combine multiple routes you cannot just concat the strings.
     * They need to be decoded back to a list LatLng and together encoded back to string.
     * @param routes routes to combine
     * @return encoded string of the combined routes
     */
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

    /**
     * Tries the save the packages in a transfer.
     */
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
