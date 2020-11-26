package hu.bme.aut.freelancer_spring.controller;

import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.dto.NavigationDto;
import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping
    ResponseEntity<List<Transfer>> findAll() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @PostMapping
    ResponseEntity<Long> save(@RequestBody TransferDto transferDto) {
        return ResponseEntity.ok(transferService.save(transferDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.delete(id));
    }
  
    @GetMapping("/packages/{id}")
    ResponseEntity<List<Package>> getPackages(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getPackages(id));
    }

    @GetMapping("/navigationUrl/{id}")
    ResponseEntity<NavigationDto> getNavigationUrl(@PathVariable Long id,
                                                   @RequestParam Double originLat,
                                                   @RequestParam Double originLong) {
        return ResponseEntity.ok(transferService.getNavigationUrl(id, new LatLng(originLat, originLong)));
    }
}