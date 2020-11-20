package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.dto.JwtDto;
import hu.bme.aut.freelancer_spring.dto.UserLoginDto;
import hu.bme.aut.freelancer_spring.dto.UserRegistrationDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.repository.UserRepository;
import hu.bme.aut.freelancer_spring.security.CustomUserDetails;
import hu.bme.aut.freelancer_spring.security.JwtUtils;
import hu.bme.aut.freelancer_spring.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        return user.get();
    }

    @Override
    public Long save(UserRegistrationDto userRegistrationDto) {
        var existingUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if (existingUser.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already exists with email: " + userRegistrationDto.getEmail());

        var user = modelMapper.map(userRegistrationDto, User.class);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public JwtDto login(UserLoginDto userLoginDto) {
        try {
            authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad login credentials");
        }
        final CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userLoginDto.getEmail());
        return new JwtDto(userDetails.getUserId(), jwtUtils.generateToken(userDetails), jwtUtils.getExpiresIn());
    }

    @Override
    public boolean delete(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        }
        userRepository.delete(user.get());
        return true;
    }

    @Override
    public boolean changeInsurance(Long id, boolean newInsurance) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        }
        var u = user.get();
        u.setHasInsurance(newInsurance);
        userRepository.save(u);
        return true;
    }

    @Override
    public List<Package> getPackages(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        return user.get().getPackages();
    }

    @Override
    public List<Transfer> getTransfers(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        return user.get().getTransfers();
    }

    @Override
    public List<Vehicle> getVehicles(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found with id: " + id);
        return user.get().getVehicles();
    }
}
