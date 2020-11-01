package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Town;

import java.util.List;

public interface TownService {

    List<Town> findAll();

    Long save(Town town);

    boolean delete(Long id);
}
