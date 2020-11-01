package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.model.Vehicle;
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
    public User findById(Long id) {
        var user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public Long save(User user) {
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public boolean delete(Long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Package> getPackages(Long id) {
        var user = userRepository.findById(id);
        return user.map(User::getPackages).orElse(null);
    }

    @Override
    public List<Transfer> getTransfers(Long id) {
        var user = userRepository.findById(id);
        return user.map(User::getTransfers).orElse(null);
    }

    @Override
    public List<Vehicle> getVehicles(Long id) {
        var user = userRepository.findById(id);
        return user.map(User::getVehicles).orElse(null);
    }
}
