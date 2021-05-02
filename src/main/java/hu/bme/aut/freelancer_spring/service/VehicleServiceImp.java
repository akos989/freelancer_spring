package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.VehicleDto;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import hu.bme.aut.freelancer_spring.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class VehicleServiceImp implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Transactional
    @Override
    public Long save(VehicleDto vehicleDto) {
        var owner = userRepository.findById(vehicleDto.getOwnerId());
        if (owner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner user not found with id: " + vehicleDto.getOwnerId());
        }
        Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
        vehicleRepository.save(vehicle);
        return vehicle.getId();
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        var vehicle = vehicleRepository.findById(id);
        if (vehicle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found with id: " + id);
        }
        vehicleRepository.delete(vehicle.get());
        return true;
    }

    @Override
    public List<Transfer> getTransfers(Long id) {
        var vehicle = vehicleRepository.findById(id);
        if (vehicle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found with id: " + id);
        }
        return vehicle.get().getTransfers();
    }
}
