package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.dto.PackageDto;
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
    public ResponseEntity<List<PackageDto>> findAll() {
        return ResponseEntity.ok(packageService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody PackageDto packageDto) {
        Long id = packageService.save(packageDto);
        if (id != null) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        var isDeleted = packageService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("update_transfer/{id}")
    public ResponseEntity updateTransferId(@PathVariable Long id, @RequestBody Long transferId) {
        var isUpdated = packageService.updateTransferId(id, transferId);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
