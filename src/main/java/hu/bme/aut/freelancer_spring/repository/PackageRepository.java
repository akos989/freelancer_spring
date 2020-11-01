package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
}
