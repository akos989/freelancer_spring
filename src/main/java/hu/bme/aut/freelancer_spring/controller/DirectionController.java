package hu.bme.aut.freelancer_spring.controller;

import com.google.maps.model.DirectionsResult;
import hu.bme.aut.freelancer_spring.service.DirectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direction")@AllArgsConstructor
public class DirectionController {
    private final DirectionService directionService;

    @GetMapping
    public ResponseEntity getDirection() {
        return ResponseEntity.ok(directionService.getDirection());
    }
}
