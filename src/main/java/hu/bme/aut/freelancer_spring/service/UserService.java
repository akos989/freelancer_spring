package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    Long save(User user);
}
