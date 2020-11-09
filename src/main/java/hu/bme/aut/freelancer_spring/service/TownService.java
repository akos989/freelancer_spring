package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;

import java.util.List;

public interface TownService {

    List<Town> findAll();

    Long save(Town town);

    boolean delete(Long id);

    List<Package> getPackages(Long id);

    List<Transfer> getTransfers(Long id);
}
