package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody User user) {
        var id = userService.save(user);
        if (id != null)
            return ResponseEntity.ok(id);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        var isDeleted = userService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<List<Package>> getPackages(@PathVariable Long id) {
        var packages = userService.getPackages(id);
        if (packages != null) {
            return ResponseEntity.ok(packages);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<List<Transfer>> getTransfers(@PathVariable Long id) {
        var transfers = userService.getTransfers(id);
        if (transfers != null) {
            return ResponseEntity.ok(transfers);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<List<Vehicle>> getVehicles(@PathVariable Long id) {
        var vehicles = userService.getVehicles(id);
        if (vehicles != null) {
            return ResponseEntity.ok(vehicles);
        }
        return ResponseEntity.notFound().build();
    }
}
