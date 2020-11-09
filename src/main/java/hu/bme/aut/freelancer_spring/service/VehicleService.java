package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.VehicleDto;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> findAll();

    Long save(VehicleDto vehicleDto);

    boolean delete(Long id);

    List<Transfer> getTransfers(Long id);
}
