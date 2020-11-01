package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.dto.VehicleDto;
import hu.bme.aut.freelancer_spring.model.Vehicle;
import hu.bme.aut.freelancer_spring.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    ResponseEntity<List<Vehicle>> findAll() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PostMapping
    ResponseEntity<Long> save(@RequestBody VehicleDto vehicleDto) {
        Long id = vehicleService.save(vehicleDto);
        if (id != null) {
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        var isDeleted = vehicleService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
