package Project.Controller;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.*;
import Project.Handler.Notification.NotificationHandler;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.Type;
import Project.Model.Notification.NotificationForm;
import Project.Model.Notification.NotificationMessage;
import Project.Model.Person.Driver;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
import Project.Model.Person.Teacher;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
import Project.Model.School;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.PositionPersistent;
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
    @Autowired
    ApiCall apiCall;
    @Autowired
    DriverHandler driverHandler;
    @Autowired
    TeacherHandler teacherHandler;
    @Autowired
    PositionPersistent positionPersistent;


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
                            @RequestParam(value = "status") Status status,
                            @RequestParam(value = "timestamp") long timestamp
    ) {
        Bus bus = busHandler.getCurrentBusPosition(carId);
        long previousTimestamp = busHandler.getCurrentTimeStamp(carId);
        boolean result = positionHandler.addBusPosition(carId, latitude, longitude, status, new Timestamp(timestamp));
        double averageVelocity = positionHandler.setVelocity(carId, bus.getCurrentLatitude(), bus.getCurrentLongitude(), previousTimestamp, latitude, longitude, timestamp);
        System.out.println("Velocity : "+positionHandler.haverSineDistance(bus.getCurrentLatitude(),bus.getCurrentLongitude(),latitude,longitude)/((timestamp - previousTimestamp)/1000));
        System.out.println("Old Avg Velocity : "+averageVelocity);
        Route route = busHandler.getBusRoutinelyUsedRoute(carId);
        if(route.getLatitudes().size() != 0) {
            for (int i = 0; i < route.getLatitudes().size(); i++) {
                //student.setAddresses(personPersistent.getPersonAddressesByPersonId(student.getId()));
                //ArrayList<Address> addresses = student.getAddresses();
                //Address address = addresses.get(0);
                if (route.getActive().get(i).equals("YES")) {
                    if((route.getType() == Type.SCHOOL && positionPersistent.isInBus(route.getPersonIds().get(i)) != IsInBus.NO)|| (route.getType() == Type.HOME && positionPersistent.isInBus(route.getPersonIds().get(i)) != IsInBus.NO )){
                    double homeLat = route.getLatitudes().get(i);
                    double homeLng = route.getLongitudes().get(i);
                    int indexTarget = i;
                        if(route.getType() == Type.SCHOOL){
                            homeLat = route.getLongitudes().get(route.getLatitudes().size()-1);
                            homeLng = route.getLongitudes().get(route.getLongitudes().size()-1);
                            indexTarget = route.getLongitudes().size()-1;
                        }
                    double dist = positionHandler.haverSineDistance(homeLat, homeLng, latitude, longitude);
                    int personSId = route.getPersonIds().get(i);
                    Student student = studentHandler.getStudentByPersonId(personSId);
                    ArrayList<Person> persons = personHandler.getPersonsRelatedToStudent(personSId);
                    JSONObject estimateTime = new JSONObject(positionHandler.estimateTime(carId, averageVelocity, route, indexTarget, latitude, longitude));
                    System.out.println("########");
                    System.out.println(estimateTime.toString());
                    for (Person person : persons) {
                        System.out.println(dist);
                        System.out.println(studentHandler.getNearHome(personSId));
                        int duration;
                        if (person.getRole() == Role.TEACHER) {
                            duration = personHandler.getTeacherAlarm(person.getId(), student.getId());
                        } else {
                            duration = personHandler.getParentAlarm(person.getId(), student.getId());
                        }
                        if (dist <= 300 && route.getType() == Type.HOME) {
                            studentHandler.setNearHome(personSId, "YES");
                        }
                        if (dist >= 500 && studentHandler.getNearHome(personSId).equals("YES") && route.getType() == Type.HOME) {
                            studentHandler.setNearHome(personSId, "NO");
                            NotificationMessage notificationMessage = new NotificationMessage();
                            notificationMessage.setTo(person.getToken());
                            NotificationForm notificationForm = new NotificationForm();
                            notificationForm.setTitle("Danger Alert");
                            Driver driver = driverHandler.getLatestDriverInBusByCarId(carId);
                            String body = "Your student: " + student.getFirstName() + " is still on the bus.\nPlease contact " + driver.getFirstName() + " (driver) " + driver.getTel();
                            ArrayList<Teacher> teacher = teacherHandler.getCurrentAllTeacherByCarId(carId);
                            for (Teacher it : teacher) {
                                body += "\n" + it.getFirstName() + "(teacher) " + it.getTel();
                            }
                            notificationForm.setBody(body);
                            notificationMessage.setNotification(notificationForm);
                            apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);

                        }
                        if (duration != -1 && estimateTime.getDouble("time") <= duration * 60) {
                            String carNumber = busHandler.getBusCarNumberByCarId(carId);
                            notificationHandler.alarm(carNumber, person.getToken(), student.getFirstName(), student.getSurName());
                            personHandler.setAlarm(person.getId(), student.getId(), -1);
                            //set alarm here
                        }
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
        if(dist >= 300 && isStudent){
            isInBus = IsInBus.TEMP;
        }
        result.put("inBus",positionHandler.getOnOrOffBus(carId, personId, latitude, longitude, isStudent, isInBus));
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
            it.setFrontImage(busHandler.getImageFromBus(it.getCarId(),true));
            it.setBackImage(busHandler.getImageFromBus(it.getCarId(),false));
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
    String estimateTime(
            @RequestParam(value = "personId") int personId
    ) {

        Bus checkBus = busHandler.getCurrentBusCarIdByStudentId(personId);
        if(positionPersistent.isInBus(personId) == IsInBus.NO){
            JSONObject result = new JSONObject();
            result.put("time", "-1");
            result.put("distance", "-1");
            return result.toString();
        }
        if(checkBus != null) {
            int carId = checkBus.getCarId();
            Route route = busHandler.getBusRoutinelyUsedRoute(carId);
            if(route.getLatitudes().size() == 0){
                JSONObject result = new JSONObject();
                result.put("time", "-1");
                result.put("distance", "-1");
                return result.toString();
            }
            Bus bus = busHandler.getCurrentBusPosition(carId);
            double velocity = busHandler.getAverageVelocity(carId);
            if (velocity == 0) {
                JSONObject result = new JSONObject();
                result.put("time", "-1");
                result.put("distance", "-1");
                return result.toString();
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
            return positionHandler.estimateTime(carId, velocity, route, index, currentLatitude, currentLongitude);
        }
        JSONObject result = new JSONObject();
        result.put("time", "-1");
        result.put("distance", "-1");
        return result.toString();
    }


}
