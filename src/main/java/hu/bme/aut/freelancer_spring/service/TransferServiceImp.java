package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Long save(TransferDto transferDto) {
        var carrier = userRepository.findById(transferDto.getCarrierId());
        var town = townRepository.findById(transferDto.getTownId());
        var vehicle = vehicleRepository.findById(transferDto.getVehicleId());

        if (carrier.isPresent() && town.isPresent() && vehicle.isPresent()) {
            if (!carrier.get().getId().equals(vehicle.get().getOwner().getId())) {
                throw new IllegalArgumentException("Car has to be the carrier!");
            }
            var transfer = modelMapper.map(transferDto, Transfer.class);
            transfer.setCarrier(carrier.get());
            transfer.setVehicle(vehicle.get());
            transfer.setTown(town.get());
            transferRepository.save(transfer);
            storePackages(transfer, findPackages(transfer));
            return transfer.getId();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        var transfer = transferRepository.findById(id);
        if (transfer.isPresent()) {
            transferRepository.delete(transfer.get());
        return true;
        }
        return false;
    }

    @Override
    public List<Package> getPackages(Long id) {
        var transfer = transferRepository.findById(id);
        return transfer.map(Transfer::getPackages).orElse(null);
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
