package Project.Controller;

import Project.Handler.Information.BusHandler;
import Project.Handler.Information.PersonHandler;
import Project.Handler.Information.StudentHandler;
import Project.Handler.Notification.NotificationHandler;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.Type;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
import Project.Model.Position.Address;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
import Project.Persistent.SQL.PersonPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by User on 1/1/2560.
 */
@RestController
public class PositionController {

    @Autowired
    BusHandler busHandler;
    @Autowired
    StudentHandler studentHandler;
    @Autowired
    PositionHandler positionHandler;
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    PersonHandler personHandler;
    @Autowired
    NotificationHandler notificationHandler;

    @RequestMapping(value = "getBusCurrentPosition", method = RequestMethod.POST)
    public
    @ResponseBody
    Bus getBusCurrentPosition(
            @RequestParam(value = "carId") int carId
    ) {
        Bus bus =  busHandler.getCurrentBusPosition(carId);
        bus.setCarNumber(busHandler.getBusCarNumberByCarId(carId));
        return bus;
    }

    @RequestMapping(value = "timeEstimation", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and body not yet finished
    String timeEstimation(
            @RequestParam(value = "carId") int carId
    ) {
        return "hello";
    }

    @RequestMapping(value = "getCurrentRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Position> getCurrentRoute(
                                         @RequestParam(value = "personId") int personId
    ) {
        Timestamp atTime = positionHandler.getLatestAtTimeByStudentId(personId);
        int carId = busHandler.getBusCarIdByStudentIdAndAtTime(personId, atTime).getCarId();
        return positionHandler.getActualRouteInTripByAtTime(carId, atTime);
    }

    @RequestMapping(value = "addBusPosition", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and body not yet finished
    boolean addBusPosition( //get all possible route to the student's home or the school
                            @RequestParam(value = "carId") int carId,
                            @RequestParam(value = "latitude") double latitude,
                            @RequestParam(value = "longitude") double longitude,
                            @RequestParam(value = "status") Status status
    ) {
        Bus bus = busHandler.getCurrentBusPosition(carId);
        boolean result = positionHandler.addBusPosition(carId, latitude, longitude, status);
        double averageVelocity = positionHandler.setVelocity(carId, bus.getCurrentLatitude(), bus.getCurrentLongitude(), latitude, longitude);
        Route route = busHandler.getBusRoutinelyUsedRoute(carId);
        for (int i = 0; i < route.getLatitudes().size(); i++) {
            //student.setAddresses(personPersistent.getPersonAddressesByPersonId(student.getId()));
            int personSId = route.getPersonId().get(i);
            Student student = studentHandler.getStudentByPersonId(personSId);
            ArrayList<Person> persons = personHandler.getPersonsRelatedToStudent(personSId);
            //ArrayList<Address> addresses = student.getAddresses();
            //Address address = addresses.get(0);
            if(route.getActive().get(i).equals("YES")) {
                double studentLatitude = route.getLatitudes().get(i);
                double studentLongitude = route.getLongitudes().get(i);
                boolean temporary = route.getTemporary().get(i).equals("YES") ? true: false;
                double estimateTime = positionHandler.estimateTime(averageVelocity, studentLatitude, studentLongitude, latitude, longitude, route, temporary);
                for (Person person : persons) {
                    int duration = personHandler.getPersonAlarm(student.getId());
                    if (duration != -1 && estimateTime <= duration * 60) {
                        String carNumber = busHandler.getBusCarNumberByCarId(carId);
                        notificationHandler.alarm(carNumber, person.getToken(), student.getFirstName(), student.getSurName());
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "getOnOrOffBus", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and body not yet finished
    boolean getOnOrOffBus( //get all possible route to the student's home or the school
                            @RequestParam(value = "carId") int carId,
                            @RequestParam(value = "personId") int personId,
                            @RequestParam(value = "latitude") double latitude,
                            @RequestParam(value = "longitude") double longitude,
                            @RequestParam(value = "isStudent") boolean isStudent,
                            @RequestParam(value = "isInBus", required = false) IsInBus isInBus
    ) {
        return positionHandler.getOnOrOffBus(carId, personId, latitude, longitude, isStudent, isInBus);
    }

    @RequestMapping(value = "addRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer addRoute(
            @RequestParam(value = "latitudes") ArrayList<Double> latitudes,
            @RequestParam(value = "longitudes") ArrayList<Double> longitudes,
            @RequestParam(value = "type") Type type,
            @RequestParam(value = "active") String active,
            @RequestParam(value = "personId") int personId
            ) {
        return positionHandler.addRoute(latitudes, longitudes, type, active, personId);
    }

    @RequestMapping(value = "getAllBusRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Route> getAllBusRoute() {
        return positionHandler.getAllBusRoute();
    }

    @RequestMapping(value = "deleteRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean deleteRoute(
            @RequestParam(value = "routeNumber") int routeNumber
    ) {
        return positionHandler.deleteRoute(routeNumber);
    }

    @RequestMapping(value = "getAllCurrentBusPosition", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Bus> getAllCurrentBusPosition(

    ) {
        ArrayList<Bus> buses = positionHandler.getAllCurrentBusPosition();
        for(Bus it: buses){
            it.setCarNumber(busHandler.getBusCarNumberByCarId(it.getCarId()));
        }
        return buses;
    }

    @RequestMapping(value = "setBusRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean setBusRoute(
            @RequestParam(value = "carId") int carId,
            @RequestParam(value = "routeNumber") int routeNumber
    ) {
        return positionHandler.setBusRoute(carId, routeNumber);
    }

    @RequestMapping(value = "estimateTime", method = RequestMethod.POST)
    public
    @ResponseBody
    double setBusRoute(
            @RequestParam(value = "carId") int carId,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "temporary") boolean temporary
    ) {
        Route route = busHandler.getBusRoutinelyUsedRoute(carId);
        Bus bus = busHandler.getCurrentBusPosition(carId);
        double velocity = busHandler.getAverageVelocity(carId);
        if(velocity == 0){
            return -1;
        }
        double currentLatitude = bus.getCurrentLatitude();
        double currentLongitude = bus.getCurrentLongitude();
        return positionHandler.estimateTime(velocity, latitude, longitude, currentLatitude, currentLongitude, route, temporary);
    }
}
