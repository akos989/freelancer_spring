package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
