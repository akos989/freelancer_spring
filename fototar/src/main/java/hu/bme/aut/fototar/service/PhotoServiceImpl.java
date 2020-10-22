package hu.bme.aut.fototar.service;

import hu.bme.aut.fototar.model.Photo;
import hu.bme.aut.fototar.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> findAll() {
        return this.photoRepository.findAll();
    }

    public Photo save(Photo photo) {
        photoRepository.save(photo);
        return photo;
    }
}
