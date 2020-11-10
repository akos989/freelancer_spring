package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.JwtDto;
import hu.bme.aut.freelancer_spring.dto.UserLoginDto;
import hu.bme.aut.freelancer_spring.dto.UserRegistrationDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.model.Vehicle;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    Long save(UserRegistrationDto userRegistrationDto);

    JwtDto login(UserLoginDto userLoginDto);

    boolean delete(Long id);

    List<Package> getPackages(Long id);

    List<Transfer> getTransfers(Long id);

    List<Vehicle> getVehicles(Long id);
}
