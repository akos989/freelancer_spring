package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Town;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.service.TownService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity save(@RequestBody Town town) {
        var exists = townService.save(town);
        if (exists == -1L) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        var isDeleted = townService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<List<Transfer>> getTransfers(@PathVariable Long id) {
        var transfers = townService.getTransfers(id);
        if (transfers != null) {
            return ResponseEntity.ok(transfers);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<List<Package>> getPackages(@PathVariable Long id) {
        var packages = townService.getPackages(id);
        if (packages != null) {
            return ResponseEntity.ok(packages);
        }
        return ResponseEntity.notFound().build();
    }

}
