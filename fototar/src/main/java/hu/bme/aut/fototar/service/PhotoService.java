package hu.bme.aut.fototar.service;

import hu.bme.aut.fototar.model.Photo;

import java.util.List;

public interface PhotoService {
    List<Photo> findAll();

    Photo save(Photo photo);
}
