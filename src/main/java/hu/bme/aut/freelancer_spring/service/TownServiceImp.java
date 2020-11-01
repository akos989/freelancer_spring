package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.repository.TownRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TownServiceImp implements TownService {

    private final TownRepository townRepository;

    @Override
    public List<Town> findAll() {
        return townRepository.findAll();
    }

    @Override
    public Long save(Town town) {
        var exists = townRepository.findByName(town.getName());
        if (exists.isEmpty()) {
            townRepository.save(town);
            return town.getId();
        }
        return -1L;
    }

    @Override
    public boolean delete(Long id) {
        var town = townRepository.findById(id);
        if (town.isPresent()) {
            townRepository.delete(town.get());
            return true;
        }
        return false;
    }
}
