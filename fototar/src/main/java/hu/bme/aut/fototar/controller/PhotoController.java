package hu.bme.aut.fototar.controller;

import hu.bme.aut.fototar.model.Photo;
import hu.bme.aut.fototar.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping
    public ResponseEntity<List<Photo>> findAll() {
        return ResponseEntity.ok(photoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Photo> save(@RequestBody Photo photo) {
        return ResponseEntity.ok(photoService.save(photo));
    }
}