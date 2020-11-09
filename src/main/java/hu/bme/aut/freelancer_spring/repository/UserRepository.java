package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
