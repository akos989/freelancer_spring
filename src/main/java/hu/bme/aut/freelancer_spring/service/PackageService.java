package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.enums.Status;

import java.util.List;

public interface PackageService {

    List<Package> findAll();

    Long save(PackageDto packageDto);

    boolean delete(Long id);

    boolean changeStatus(Long packageId, Status status);
}
