package hu.bme.aut.fototar.repository;

import hu.bme.aut.fototar.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    //    List<Photo> findAllByName
    List<Photo> findAllByPriceBetween(int a, int b);
}
