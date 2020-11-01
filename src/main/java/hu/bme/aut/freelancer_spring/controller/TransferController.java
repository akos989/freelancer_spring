package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.dto.TransferDto;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            var id = transferService.save(transferDto);
            if (id != null) {
                return ResponseEntity.ok(id);
            }
            return ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        var isDeleted = transferService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
