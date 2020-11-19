package hu.bme.aut.freelancer_spring.service;

import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;

import java.util.List;

public interface TransferService {

    List<Transfer> findAll();

    Long save(TransferDto transferDto);

    boolean delete(Long id);

    List<Package> getPackages(Long id);

    List<LatLng> calculateRoute(Long id);
}
