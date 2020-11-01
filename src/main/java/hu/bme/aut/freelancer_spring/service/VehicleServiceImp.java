package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.VehicleDto;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import hu.bme.aut.freelancer_spring.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public Long save(VehicleDto vehicleDto) {
        var owner = userRepository.findById(vehicleDto.getOwnerId());
        if (owner.isPresent()) {
            Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
            vehicleRepository.save(vehicle);
            return vehicle.getId();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        var vehicle = vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            vehicleRepository.delete(vehicle.get());
            return true;
        }
        return false;
    }
}
