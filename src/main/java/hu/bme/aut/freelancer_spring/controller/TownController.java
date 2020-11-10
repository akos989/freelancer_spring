package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.service.TownService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/towns")
@AllArgsConstructor
public class TownController {

    private final TownService townService;

    @GetMapping
    public ResponseEntity<List<Town>> findAll() {
        return ResponseEntity.ok(townService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody Town town) {
        return ResponseEntity.ok(townService.save(town));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(townService.delete(id));
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<List<Transfer>> getTransfers(@PathVariable Long id) {
        return ResponseEntity.ok(townService.getTransfers(id));
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<List<Package>> getPackages(@PathVariable Long id) {
        return ResponseEntity.ok(townService.getPackages(id));
    }
}
