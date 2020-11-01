package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
