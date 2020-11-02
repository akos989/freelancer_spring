package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByTownAndDateAfterOrderByDateAscCreatedAtAsc(Town town, Date createdAt);
}