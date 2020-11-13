package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import hu.bme.aut.freelancer_spring.repository.PackageRepository;
import hu.bme.aut.freelancer_spring.repository.TransferRepository;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PackageServiceImp implements PackageService {

    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TransferRepository transferRepository;

    @Override
    public List<Package> findAll() {
        return packageRepository.findAll();
    }

    @Override
    public Long save(PackageDto packageDto) {
        var sender = userRepository.findById(packageDto.getSenderId());
        if (sender.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found with id: " + packageDto.getSenderId());
        }
        Package pack = modelMapper.map(packageDto, Package.class);
        findTransfer(pack).ifPresent(pack::setTransfer);
        packageRepository.save(pack);
        return pack.getId();
    }

    @Override
    public boolean delete(Long id) {
        var pack = packageRepository.findById(id);
        if (pack.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found with id: " + id);
        }
        packageRepository.delete(pack.get());
        return true;
    }

    @Override
    public boolean changeStatus(Long packageId, Status status) {
        var pack = packageRepository.findById(packageId);
        if (pack.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found with id: " + packageId);
        }
        var p = pack.get();
        p.setStatus(status);
        packageRepository.save(p);
        return true;
    }

    private Optional<Transfer> findTransfer(Package pack) {
        var transfers =
                transferRepository.findAllByTownAndDateAfterOrderByDateAscCreatedAtAsc(pack.getTown(), pack.getCreatedAt());

        return transfers.stream()
                .filter(t -> t.fitPackage(pack))
                .findFirst();
    }
}
