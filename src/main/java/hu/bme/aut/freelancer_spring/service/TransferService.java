package hu.bme.aut.freelancer_spring.service;

import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.dto.NavigationDto;
import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;

import java.util.Date;
import java.util.List;

public interface TransferService {

    List<Transfer> findAll();

    Long save(TransferDto transferDto);

    boolean delete(Long id);

    List<Package> getPackages(Long id);

    List<Transfer> getTransfersOnDate(Date date);

    void calculateRoute(Transfer transfer);

    NavigationDto getNavigationUrl(Long id, LatLng origin);
}
