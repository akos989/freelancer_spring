package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Long save(User user) {
        userRepository.save(user);
        return user.getId();
    }
}
