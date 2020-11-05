package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByTransferIsNullAndTownAndDateLimitAfterOrderByCreatedAt(Town town, Date date);
}
