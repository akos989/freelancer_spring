package hu.bme.aut.freelancer_spring.service;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.model.Transfer;

import java.util.List;

public interface DirectionService {

    List<DirectionsRoute> getRouteForTransfer(Transfer transfer, LatLng origin);
}