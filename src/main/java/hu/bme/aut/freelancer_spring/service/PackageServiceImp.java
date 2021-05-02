package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import hu.bme.aut.freelancer_spring.repository.PackageRepository;
import hu.bme.aut.freelancer_spring.repository.TownRepository;
import hu.bme.aut.freelancer_spring.repository.TransferRepository;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PackageServiceImp implements PackageService {

    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final TownRepository townRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TransferRepository transferRepository;

    @Override
    public List<Package> findAll() {
        return packageRepository.findAll();
    }

    @Transactional
    @Override
    public Long save(PackageDto packageDto) {
        var sender = userRepository.findById(packageDto.getSenderId());
        var town = townRepository.findById(packageDto.getTownId());
        if (sender.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found with id: " + packageDto.getSenderId());
        }
        if (town.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Town not found with id: " + packageDto.getTownId());
        }
        Package pack = modelMapper.map(packageDto, Package.class);
        pack.setTown(town.get());
        findTransfer(pack).ifPresent(pack::setTransfer);
        packageRepository.save(pack);
        return pack.getId();
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        var pack = packageRepository.findById(id);
        if (pack.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found with id: " + id);
        }
        packageRepository.delete(pack.get());
        return true;
    }

    @Transactional
    @Override
    public boolean changeStatus(Long packageId, Status status) {
        var pack = packageRepository.findById(packageId);
        if (pack.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found with id: " + packageId);
        }
        var p = pack.get();
        p.setStatus(status);
        packageRepository.save(p);
        return true;
    }

    /**
     * Automatically called when a package is created. Looks for transfers in the db and tries the place the package in one.
     * @param pack newly created package
     * @return Optional object which holds a Transfer if one was found
     */
    private Optional<Transfer> findTransfer(Package pack) {
        var town = pack.getTown();
        var createdAT = pack.getCreatedAt();
        var transfers =
                transferRepository.findAllByTownAndDateAfterOrderByDateAscCreatedAtAsc(pack.getTown(), pack.getCreatedAt());
        return transfers.stream()
                .filter(t -> t.fitPackage(pack))
                .findFirst();
    }
}
