package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.PackageDto;

import java.util.List;

public interface PackageService {

    List<PackageDto> findAll();

    Long save(PackageDto packageDto);

    boolean delete(Long id);

    boolean updateTransferId(Long packageId, Long transferId);
}
