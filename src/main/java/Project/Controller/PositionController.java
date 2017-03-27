package Project.Controller;

import Project.Handler.Information.BusHandler;
import Project.Handler.Information.PersonHandler;
import Project.Handler.Information.SchoolHandler;
import Project.Handler.Information.StudentHandler;
import Project.Handler.Notification.NotificationHandler;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.Type;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
import Project.Model.School;
import Project.Persistent.SQL.PersonPersistent;
import org.json.JSONObject;
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
    @Autowired
    SchoolHandler schoolHandler;

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
        if(atTime != null) {
            int carId = busHandler.getBusCarIdByStudentIdAndAtTime(personId, atTime).getCarId();
            return positionHandler.getActualRouteInTripByAtTime(carId, atTime);
        }
        return new ArrayList<>();
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
        if(route.getLatitudes().size() == 0) {
            for (int i = 0; i < route.getLatitudes().size(); i++) {
                //student.setAddresses(personPersistent.getPersonAddressesByPersonId(student.getId()));
                //ArrayList<Address> addresses = student.getAddresses();
                //Address address = addresses.get(0);
                if (route.getActive().get(i).equals("YES")) {
                    int personSId = route.getPersonIds().get(i);
                    Student student = studentHandler.getStudentByPersonId(personSId);
                    ArrayList<Person> persons = personHandler.getPersonsRelatedToStudent(personSId);
                    double estimateTime = positionHandler.estimateTime(averageVelocity, route, i, latitude, longitude);
                    for (Person person : persons) {
                        int duration = personHandler.getPersonAlarm(student.getId());
                        if (duration != -1 && estimateTime <= duration * 60) {
                            String carNumber = busHandler.getBusCarNumberByCarId(carId);
                            notificationHandler.alarm(carNumber, person.getToken(), student.getFirstName(), student.getSurName());
                        }
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
    String getOnOrOffBus( //get all possible route to the student's home or the school
                            @RequestParam(value = "carId") int carId,
                            @RequestParam(value = "personId") int personId,
                            @RequestParam(value = "latitude") double latitude,
                            @RequestParam(value = "longitude") double longitude,
                            @RequestParam(value = "isStudent") boolean isStudent,
                            @RequestParam(value = "isInBus", required = false) IsInBus isInBus
    ) {
        JSONObject result = new JSONObject();
        Route r = busHandler.getBusRoutinelyUsedRoute(carId);
        double homeLat = 0;
        double homeLng = 0;
        int i = 0;
        if(r.getType() == Type.HOME) {
            while (i < r.getLatitudes().size()) {
                if (r.getActive().get(i).equals("YES") && r.getPersonIds().get(i) == personId) {
                    homeLat = r.getLatitudes().get(i);
                    homeLng = r.getLongitudes().get(i);
                }
                i++;
            }
        }
        else{
            School school = schoolHandler.getSchoolDetail("KMITL");
            homeLat = school.getLatitude();
            homeLng = school.getLongitude();
        }
        double dist = positionHandler.haverSineDistance(latitude,longitude,homeLat,homeLng);
        if(dist >= 100){
            isInBus = IsInBus.TEMP;
        }
        result.put("isInBus",positionHandler.getOnOrOffBus(carId, personId, latitude, longitude, isStudent, isInBus));
        //
        result.put("personId", personId);
        Person person = personHandler.getPersonByPersonId(personId);
        result.put("firstName", person.getFirstName());
        result.put("surname", person.getSurName());
        return result.toString();
    }

    @RequestMapping(value = "addRoute", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer addRoute(
            @RequestParam(value = "latitudes") ArrayList<Double> latitudes,
            @RequestParam(value = "longitudes") ArrayList<Double> longitudes,
            @RequestParam(value = "type") Type type,
            @RequestParam(value = "active") ArrayList<String> active,
            @RequestParam(value = "temporary") ArrayList<String> temporary,
            @RequestParam(value = "personIds") ArrayList<Integer> personIds
            ) {
        return positionHandler.addRoute(latitudes, longitudes, type, active, personIds, temporary);
    }

    @RequestMapping(value = "addTemporary", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean addTemporaryRoute(
            @RequestParam(value = "routeNumber") int routeNumber,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "active") String active,
            @RequestParam(value = "temporary") String temporary,
            @RequestParam(value = "personId") int personId
    ) {
        return positionHandler.addTemporaryRoute(routeNumber, latitude, longitude, active, personId, temporary);
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
    double estimateTime(
            @RequestParam(value = "personId") int personId
    ) {
        Bus checkBus = busHandler.getCurrentBusCarIdByStudentId(personId);
        if(checkBus != null) {
            int carId = checkBus.getCarId();
            Route route = busHandler.getBusRoutinelyUsedRoute(carId);
            if(route.getLatitudes().size() == 0){
                return -1;
            }
            Bus bus = busHandler.getCurrentBusPosition(carId);
            double velocity = busHandler.getAverageVelocity(carId);
            if (velocity == 0) {
                return -1;
            }
            double currentLatitude = bus.getCurrentLatitude();
            double currentLongitude = bus.getCurrentLongitude();
            int index = 0;
            if(route.getType() == Type.HOME) {
                for (int i = 0; i < route.getPersonIds().size(); i++) {
                    if (route.getPersonIds().get(i) == personId) {
                        index = i;
                        i = route.getPersonIds().size();
                    }
                }
            }else{
                index = route.getActive().size() - 1;
            }
            return positionHandler.estimateTime(velocity, route, index, currentLatitude, currentLongitude);
        }
        return -1;
    }
}
