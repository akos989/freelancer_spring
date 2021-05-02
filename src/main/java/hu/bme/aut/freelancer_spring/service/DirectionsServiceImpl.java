package hu.bme.aut.freelancer_spring.service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DirectionsServiceImpl implements DirectionService {

    private final GeoApiContext geoApiContext = new GeoApiContext
            .Builder()
            .apiKey("AIzaSyBPYPWC__LjvY2Dyi93o4K3NV5yjhNOXIU") // this API key is not active anymore, so route calculating functions are not available
            .build();

    /**
     * Calculates the optimal route for a transfer from a given starting point. This is done in the phases.
     * In the 1st one finds the route to pick up all the packages.
     * In the 2nd ont finds the route to deliver the packages.
     * @param transfer: the transfer for which the route is calculated
     * @param origin: the starting point in LatLng
     * @return two DirectionRoute objects with the two phases
     */
    @Override
    public List<DirectionsRoute> getRouteForTransfer(Transfer transfer, LatLng origin) {
        var packages = transfer.getPackages();
        var waitingNum = packages.stream()
                .filter(p -> p.getStatus() == Status.WAITING)
                .count();
        var inCarNum = packages.stream()
                .filter(p -> p.getStatus() == Status.INCAR)
                .count();

        if (inCarNum + waitingNum == 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No packages to deliver");
        LatLng destination = new LatLng(transfer.getToLat(), transfer.getToLong());

        var pickUpRoute = new DirectionsRoute();
        DirectionsLeg[] legs = {};
        pickUpRoute.legs = legs;
        LatLng lastPickupLatLng = origin;
        // only need the get the pick up route if there are still packages waiting for pick up
        if (waitingNum != 0) {
            pickUpRoute = getPickupRoute(origin, destination, transfer.getPackages());
            int lastPackageWaypointIdx = pickUpRoute.waypointOrder[pickUpRoute.waypointOrder.length - 1];
            var lastPackage = transfer.getPackages().get(lastPackageWaypointIdx);
            lastPickupLatLng = new LatLng(lastPackage.getFromLat(), lastPackage.getFromLong());
        }
        DirectionsRoute deliveryRoute;
        if (waitingNum == 0 && inCarNum > 0) {
            deliveryRoute = getPickupRoute(origin, destination, transfer.getPackages());
        } else {
            deliveryRoute = getDeliveryRoute(lastPickupLatLng, destination, transfer.getPackages());
        }

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

    /**
     * Google API call to calculate the optimal route between waypoints.
     * @param origin first waypoint
     * @param destination last waypoints
     * @param waypoints in-between waypoints
     * @return the object which holds the route
     */
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
