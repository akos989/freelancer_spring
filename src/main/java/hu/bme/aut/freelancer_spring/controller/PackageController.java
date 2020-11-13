package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import hu.bme.aut.freelancer_spring.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@AllArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<List<Package>> findAll() {
        return ResponseEntity.ok(packageService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody PackageDto packageDto) {
        return ResponseEntity.ok(packageService.save(packageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.delete(id));
    }

    @PutMapping("changeStatus/{id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable Long id, @RequestParam("status") Status status) {
        return ResponseEntity.ok(packageService.changeStatus(id, status));
    }
}