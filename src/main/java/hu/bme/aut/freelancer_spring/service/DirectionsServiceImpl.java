package hu.bme.aut.freelancer_spring.service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DirectionsServiceImpl implements DirectionService {

    private final GeoApiContext geoApiContext = new GeoApiContext
            .Builder()
            .apiKey("AIzaSyCCRJFDFXJpHmDdFyFwd9g1yOtU9pOLQAU")
            .build();

    @Override
    public List<DirectionsRoute> getRouteForTransfer(Transfer transfer, LatLng origin) {
        LatLng destination = new LatLng(transfer.getToLat(), transfer.getToLong());

        var pickUpRoute = getPickupRoute(origin, destination, transfer.getPackages());

        int lastPackageWaypointIdx = pickUpRoute.waypointOrder[pickUpRoute.waypointOrder.length - 1];
        var lastPackage = transfer.getPackages().get(lastPackageWaypointIdx);
        LatLng lastPickupLatLng = new LatLng(lastPackage.getFromLat(), lastPackage.getFromLong());

        var deliveryRoute = getDeliveryRoute(lastPickupLatLng, destination, transfer.getPackages());

        var routeList = new ArrayList<DirectionsRoute>();
        routeList.add(pickUpRoute);
        routeList.add(deliveryRoute);
        return routeList;
    }

    private DirectionsRoute getPickupRoute(LatLng origin, LatLng destination, List<Package> packages) {
        List<LatLng> waypoints = new ArrayList<>();
        packages.forEach(p -> {
            if (p.getStatus() == Status.WAITING) {
                waypoints.add(new LatLng(p.getFromLat(), p.getFromLong()));
            }
            else if (p.getStatus() == Status.INCAR) {
                waypoints.add(new LatLng(p.getToLat(), p.getToLong()));
            }
        });

        return getDirectionResult(origin, destination, waypoints).routes[0];
    }

    private DirectionsRoute getDeliveryRoute(LatLng origin, LatLng destination, List<Package> packages) {
        List<LatLng> waypoints = new ArrayList<>();
        packages.stream()
                .filter(p -> p.getStatus() == Status.WAITING)
                .forEach(p -> waypoints.add(new LatLng(p.getToLat(), p.getToLong())));

        return getDirectionResult(origin, destination, waypoints).routes[0];
    }

    private DirectionsResult getDirectionResult(LatLng origin, LatLng destination, List<LatLng> waypoints) {
        DirectionsResult result;
        try {
            result = DirectionsApi.newRequest(geoApiContext)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(Instant.now())
                    .waypoints(waypoints.subList(0, waypoints.size()).toArray(new LatLng[0]))
                    .optimizeWaypoints(true)
                    .await();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return result;
    }
}
