package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.TownRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class TownServiceImp implements TownService {

    private final TownRepository townRepository;

    @Override
    public List<Town> findAll() {
        return townRepository.findAll();
    }

    @Transactional
    @Override
    public Long save(Town town) {
        var exists = townRepository.findByName(town.getName());
        if (exists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Town already exists with name: " + town.getName());
        }
        townRepository.save(town);
        return town.getId();
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        var town = townRepository.findById(id);
        if (town.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Town not found with id: " + id);
        }
        townRepository.delete(town.get());
        return true;
    }

    @Transactional
    @Override
    public List<Package> getPackages(Long id) {
        var town = townRepository.findById(id);
        if (town.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Town not found with id: " + id);
        }
        return town.get().getPackages();
    }

    @Transactional
    @Override
    public List<Transfer> getTransfers(Long id) {
        var town = townRepository.findById(id);
        if (town.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Town not found with id: " + id);
        }
        return town.get().getTransfers();
    }
}
