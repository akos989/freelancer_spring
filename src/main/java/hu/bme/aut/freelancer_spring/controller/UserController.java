package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.model.User;
import hu.bme.aut.freelancer_spring.service.UserService;
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

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
}
