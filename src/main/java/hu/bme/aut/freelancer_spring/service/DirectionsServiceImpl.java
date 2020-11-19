package hu.bme.aut.freelancer_spring.service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
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
            .apiKey("AIzaSyA1q369RoqolGea6FsqE0kNSPdl1JWnNFk")
            .build();

    @Override
    public List<DirectionsRoute> getRouteForTransfer(Transfer transfer) {
        LatLng origin = new LatLng(transfer.getFromLat(), transfer.getFromLong());
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
        packages.forEach(p -> waypoints.add(new LatLng(p.getFromLat(), p.getFromLong())));

        return getDirectionResult(origin, destination, waypoints).routes[0];
    }

    private DirectionsRoute getDeliveryRoute(LatLng origin, LatLng destination, List<Package> packages) {
        List<LatLng> waypoints = new ArrayList<>();
        packages.forEach(p -> waypoints.add(new LatLng(p.getToLat(), p.getToLong())));

        return getDirectionResult(origin, destination, waypoints).routes[0];
    }

    private DirectionsResult getDirectionResult(LatLng origin, LatLng destination, List<LatLng> waypoints) {
        DirectionsResult result = null;
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


    @Override
    public List<LatLng> getDirection() {
        List<LatLng> waypoints = getOptimizationWaypoints();
        LatLng origin = new LatLng(47.602634,19.241843);
        LatLng destination = new LatLng(47.602634,19.241843);
        DirectionsResult result = null;
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

        String resultLatLngs = "";
        System.out.println("----------------------------");

        int i = 0;
        LocalTime time = LocalTime.of(10,0,0);
        for (var leg : result.routes[0].legs) {
            if (i != result.routes[0].legs.length - 1) {
                time = time.plusSeconds(leg.duration.inSeconds);
                System.out.println(time);
            }
            i++;
        }

        var encodedRoute = "";
        var pointsList = new ArrayList<LatLng>();
        i = 0;
        for (var leg : result.routes[0].legs) {
            if (i != result.routes[0].legs.length - 1) {
                for (var step : leg.steps) {
                    pointsList.addAll(step.polyline.decodePath());
//                    encodedRoute += step.polyline.getEncodedPath();
                }
            }
            i++;
        }

        encodedRoute = PolylineEncoding.encode(pointsList);
        System.out.println(encodedRoute);
        var newList = PolylineEncoding.decode(encodedRoute);
        for (var latlng : newList) {
            System.out.println(latlng.toString());
        }
//        var encodedRoute = "";
//        for (var leg : result.routes[0].legs) {
//            for (var step : leg.steps) {
//                encodedRoute += step.polyline.getEncodedPath();
//            }
//        }
//        System.out.println(encodedRoute);
//        for ( var route : result.routes ) {
//            var decodedPath = PolylineEncoding()
//            for ( var leg : route.legs ) {
//                resultLatLngs += leg.startLocation.lat + "," + leg.startLocation.lng + "|";
//            }
//        }
//        System.out.println("-------");
//        System.out.println(resultLatLngs);
        return pointsList;
    }

    private List<LatLng> getOptimizationWaypoints() {
        List<LatLng> waypoints = new ArrayList<>();
        waypoints.add(new LatLng(47.552851,19.113058));
        waypoints.add(new LatLng(47.554348,19.130762));
        waypoints.add(new LatLng(47.507638, 19.024248));
//        waypoints.add(new LatLng(19.396436, -99.157176));
//        waypoints.add(new LatLng(19.427705, -99.198858));
//        waypoints.add(new LatLng(19.425869, -99.160716));
        return waypoints;
    }
}
