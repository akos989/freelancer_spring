package hu.bme.aut.freelancer_spring.repository;

import hu.bme.aut.freelancer_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
