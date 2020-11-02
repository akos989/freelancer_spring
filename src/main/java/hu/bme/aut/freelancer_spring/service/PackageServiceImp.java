package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.repository.PackageRepository;
import hu.bme.aut.freelancer_spring.repository.TransferRepository;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
        var user = userRepository.findById(packageDto.getSenderId());
        if (user.isPresent()) {
            Package pack = modelMapper.map(packageDto, Package.class);
            findTransfer(pack);
            packageRepository.save(pack);
            return pack.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        var pack = packageRepository.findById(id);
        if (pack.isPresent()) {
            packageRepository.delete(pack.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateTransferId(Long packageId, Long transferId) {
        //TODO
//        var p = packageRepository.findById(packageId);
//        p.
        return false;
    }

    private void findTransfer(Package pack) {
        var transfers =
                transferRepository.findAllByTownAndDateAfterOrderByDateAscCreatedAtAsc(pack.getTown(), pack.getCreatedAt());

        var transfer = transfers.stream()
                .filter(tran -> tran.fitPackage(pack))
                .findFirst();

        transfer.ifPresent(pack::setTransfer);
    }
}
