package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.dto.JwtDto;
import hu.bme.aut.freelancer_spring.dto.UserLoginDto;
import hu.bme.aut.freelancer_spring.dto.UserRegistrationDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Long> save(
            @RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return ResponseEntity.ok(userService.save(userRegistrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return ResponseEntity.ok(userService.login(userLoginDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @PutMapping("changeInsurance/{id}")
    public ResponseEntity changeInsurance(@PathVariable Long id, @RequestParam boolean insurance) {
        return ResponseEntity.ok(userService.changeInsurance(id, insurance));
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<List<Package>> getPackages(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPackages(id));
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<List<Transfer>> getTransfers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTransfers(id));
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<List<Vehicle>> getVehicles(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getVehicles(id));
    }
}
