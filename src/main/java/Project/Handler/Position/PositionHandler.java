package Project.Handler.Position;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.*;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.Type;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Notification.NotificationForm;
import Project.Model.Notification.NotificationMessage;
import Project.Model.Person.Driver;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
import Project.Model.Person.Teacher;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
import Project.Persistent.SQL.PositionPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

;import static java.util.stream.Collectors.toList;

/**
 * Created by User on 6/1/2560.
 */
@Service
public class PositionHandler {

    @Autowired
    PositionPersistent positionPersistent;
    @Autowired
    ApiCall apiCall;
    @Autowired
    PersonHandler personHandler;
    @Autowired
    StudentHandler studentHandler;
    @Autowired
    BusHandler busHandler;
    @Autowired
    TeacherHandler teacherHandler;
    @Autowired
    DriverHandler driverHandler;

    public ArrayList<Timestamp> getCurrentStartAndEndPeriodByStudentId(int carId, int personId) {
        ArrayList<Timestamp> period = new ArrayList<>();
        Timestamp atTime = positionPersistent.getLatestAtTimeByStudentId(personId);
        period.add(positionPersistent.getTripStartTimeInPersonInBus(carId, atTime));
        period.add(positionPersistent.getTripFinishTimeInPersonInBus(carId, atTime));
        return period;
    }

    public Timestamp getLatestAtTimeByStudentId(int personId) {
        return positionPersistent.getLatestAtTimeByStudentId(personId);
    }

    public ArrayList<Position> getActualRouteInTripByAtTime(int carId, Timestamp atTime) {
        Timestamp startTime = positionPersistent.getTripStartTimeInBusPosition(carId, atTime);
        Timestamp endTime = positionPersistent.getTripFinishTimeInBusPosition(carId, atTime);
        return positionPersistent.getActualRouteInBusByPeriod(carId, startTime, endTime);
    }

    public boolean addBusPosition(int carId, double latitude, double longitude, Status status) {
        return positionPersistent.addBusPosition(carId, latitude, longitude, status);
    }


    public Integer addRoute(ArrayList<Double> latitudes, ArrayList<Double> longitudes, Type type, ArrayList<String> active, ArrayList<Integer> personIds, ArrayList<String> temporary) {
        return positionPersistent.addRoute(latitudes, longitudes, type, active, personIds, temporary);
    }

    public boolean addTemporaryRoute(int routeNumber, double latitude, double longitude, String active, int personId, String temporary) {
        return positionPersistent.addTemporaryRoute(routeNumber, latitude, longitude, active, personId, temporary);
    }

    public boolean deleteRoute(int routeNumber) {
        return positionPersistent.deleteRoute(routeNumber);
    }

    public boolean setBusRoute(int carId, int routeNumber) {
        return positionPersistent.setBusRoute(carId, routeNumber);
    }

    public ArrayList<Route> getAllBusRoute() {
        SqlRowSet sqlRowSet = positionPersistent.getAllBusRoute();
        ArrayList<Route> routeList = new ArrayList<>();
        int current = -1;
        int next;
        Type type;
        Route route = new Route();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Integer> personIds = new ArrayList<>();
        ArrayList<String> actives = new ArrayList<>();
        ArrayList<String> temporaries = new ArrayList<>();
        while (sqlRowSet.next()) {
            next = sqlRowSet.getInt("routeNumber");
            type = Type.valueOf(sqlRowSet.getString("type"));
            if (current != next) {
                if (current != -1) {
                    routeList.add(route);
                    route.setActive(actives);
                    route.setPersonIds(personIds);
                    route.setTemporary(temporaries);
                    route.setLatitudes(latitudes);
                    route.setLongitudes(longitudes);
                }
                current = next;
                route = new Route();
                latitudes = new ArrayList<>();
                longitudes = new ArrayList<>();
                personIds = new ArrayList<>();
                actives = new ArrayList<>();
                temporaries = new ArrayList<>();
                route.setRouteNumber(current);
                route.setType(type);
            }
            latitudes.add(sqlRowSet.getDouble("latitude"));
            longitudes.add(sqlRowSet.getDouble("longitude"));
            personIds.add(sqlRowSet.getInt("personId"));
            actives.add(sqlRowSet.getString("active"));
            temporaries.add(sqlRowSet.getString("temporary"));
        }
        route.setLatitudes(latitudes);
        route.setLongitudes(longitudes);
        route.setActive(actives);
        route.setPersonIds(personIds);
        route.setTemporary(temporaries);
        routeList.add(route);
        return routeList;
    }

    public ArrayList<Bus> getAllCurrentBusPosition() {
        ArrayList<Bus> busList = busHandler.getAllBus();
        for (Bus it : busList) {
            SqlRowSet sqlRowSet = positionPersistent.getBusRouteByCarId(it.getCarId());
            Route route = new Route();
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            while (sqlRowSet.next()) {
                if (sqlRowSet.isFirst())
                    route.setRouteNumber(sqlRowSet.getInt("routeNumber"));
                latitudes.add(sqlRowSet.getDouble("latitude"));
                longitudes.add(sqlRowSet.getDouble("longitude"));
            }
            route.setLatitudes(latitudes);
            route.setLongitudes(longitudes);
            it.setCurrentRoute(route);
            Position position = positionPersistent.getCurrentBusPosition(it.getCarId());
            it.setCurrentLatitude(position.getLatitude());
            it.setCurrentLongitude(position.getLongitude());
        }
        return busList;
    }

    public boolean getOnOrOffBus(int carId, int personId, Double latitude, Double longitude, boolean isStudent, IsInBus inBus) {

        // don't forget that the bus driver isn't the last person. we count only the students.
        IsInBus isInBus = positionPersistent.isInBus(personId);
        String onOrOff = "OFF";
        if (isInBus == IsInBus.NO) {
            onOrOff = "ON";
            isInBus = IsInBus.YES;
        } else {
            isInBus = IsInBus.NO;
        }
        if(inBus != null){
            isInBus = inBus;
        }
        Status status;
        Student student;
        ArrayList<Person> userRelatedToStudent = new ArrayList<>();
        String studentName = "";
        String surname = "";
        Timestamp time, now, midNight;
        if(isStudent) {
            student = studentHandler.getStudentByPersonId(personId);
            userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId);
            studentName = student.getFirstName();
            surname = student.getSurName();
        }
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 12); //anything 0 - 23
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            time = new Timestamp(c.getTimeInMillis());
            c.set(Calendar.HOUR_OF_DAY, 0);
            now = new Timestamp(System.currentTimeMillis());
            midNight = new Timestamp(c.getTimeInMillis());

        if (isInBus != IsInBus.TEMP && positionPersistent.isFirstPerson(carId, now, time, midNight)) {
            status = Status.PERSONSTART;
            positionPersistent.addBusPosition(carId, latitude, longitude, status);
            status = Status.START;
            // can be the driver
        } else if (isStudent && isInBus == IsInBus.NO && isLastPerson(personId, carId, now, time, midNight)) {
            ArrayList<Timestamp> startAndEndPeriod = getCurrentStartAndEndPeriodByStudentId(carId, personId);
            ArrayList<Teacher> teachers = teacherHandler.getCurrentTeacherInBusByCarId(carId, startAndEndPeriod);
            Driver driver = driverHandler.getCurrentDriverInBusByCarId(carId, startAndEndPeriod);
            for(Teacher it: teachers) {
                getOnOrOffBus(carId,it.getId(), latitude, longitude, false, null);
            }
            if(driver != null)
            getOnOrOffBus(carId,driver.getId(), latitude, longitude, false, null);
            Route route = busHandler.getBusRoutinelyUsedRoute(carId);
            positionPersistent.deleteTemporary(route);
            status = Status.FINISH;
            positionPersistent.addBusPosition(carId, latitude, longitude, status);
            studentHandler.addStudentsTrip(studentHandler.getAllStudentsUsedToBeOnBusInCurrentTrip(carId, now, time, midNight), -1);
            busHandler.setVelocityToZero(carId);
            // the last student not the driver
        } else {
            status = Status.NONE;
        }
        int enterTime = positionPersistent.latestEnterTime(personId, carId /*, now, time, midNight */) + 1;
        positionPersistent.getOnOrOffBus(carId, personId, latitude, longitude, isInBus, status, enterTime);
        if(isStudent) {
            for (Person it : userRelatedToStudent) {
                NotificationMessage notificationMessage = new NotificationMessage();
                NotificationForm notificationForm = new NotificationForm();
                notificationForm.setTitle("Support System For School Bus");
                notificationForm.setBody("Your student, " + studentName + " " + surname + " " + ", has just get " + onOrOff + "The Bus Number" + carId);
                notificationMessage.setNotification(notificationForm);
                notificationMessage.setTo(it.getToken());
                apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);
            }
        }
        return isInBus != IsInBus.YES;
    }

    public boolean isLastPerson(int personId, int carId, Timestamp now, Timestamp lunch, Timestamp midNight) {
        TypeOfService typeOfService;
        if (now.getTime() >= lunch.getTime()) {
            typeOfService = TypeOfService.BACK;
        } else {
            typeOfService = TypeOfService.GO;
        }
        int studentNumber = studentHandler.getNumberOfStudentInCurrentTrip(carId, typeOfService);
        int studentNotInCarId = studentHandler.getNumberOfStudentGetOutInCurrentTripExceptPersonId(personId, carId, now, lunch, midNight);
        return studentNumber - 1 == studentNotInCarId;
    }

    public double setVelocity(int carId, double previousLatitude, double previousLongitude, double newLatitude, double newLongitude) {
        double oldAverageVelocity = busHandler.getAverageVelocity(carId);
        int checkPointPassed = busHandler.getCheckPointPassed(carId)+1;
        double averageVelocity = getAverageVelocity(oldAverageVelocity,checkPointPassed, previousLatitude, previousLongitude, newLatitude, newLongitude);
        busHandler.setVelocityAndCheckPointPassed(carId, averageVelocity, checkPointPassed);
        return averageVelocity;
    }

    public double getAverageVelocity(double oldAverageVelocity, int checkPointPassed, double previousLatitude, double previousLongitude, double newLatitude, double newLongitude){
        return (oldAverageVelocity*(checkPointPassed-1)+haverSineDistance(previousLatitude, previousLongitude, newLatitude, newLongitude))/checkPointPassed;
    }

    public double haverSineDistance(double previousLatitude, double previousLongitude, double currentLatitude, double currentLongitude){
        int earthRadius = 6378137;
        double deltaLat = previousLatitude-currentLatitude;
        double deltaLon = previousLongitude-currentLongitude;
        double a = Math.sin(deltaLat/2)* Math.sin(deltaLat/2)+Math.cos(previousLatitude)*Math.cos(currentLatitude)*Math.sin(deltaLon/2)*Math.sin(deltaLon/2);
        double c =2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = earthRadius*c;
        return d;
    }

    public Route getBusRoutinelyUsedRoute(int carId){
        SqlRowSet sqlRowSet = positionPersistent.getBusRouteByCarId(carId);
        Route route = new Route();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> actives = new ArrayList<>();
        ArrayList<Integer> personIds = new ArrayList<>();
        ArrayList<String> temporaries = new ArrayList<>();
        while (sqlRowSet.next()) {
            if (sqlRowSet.isFirst()) {
                route.setRouteNumber(sqlRowSet.getInt("routeNumber"));
            }
            latitudes.add(sqlRowSet.getDouble("latitude"));
            longitudes.add(sqlRowSet.getDouble("longitude"));
            actives.add(sqlRowSet.getString("active"));
            personIds.add(sqlRowSet.getInt("personId"));
            temporaries.add(sqlRowSet.getString("temporary"));
        }
        route.setLatitudes(latitudes);
        route.setLongitudes(longitudes);
        route.setActive(actives);
        route.setPersonIds(personIds);
        route.setTemporary(temporaries);
        return route;
    }

    public double estimateTime(double velocity, Route route, int index, double busLatitude, double busLongitude){
        ArrayList<Double> routeLatitudes = route.getLatitudes();
        ArrayList<Double> routeLongitudes = route.getLongitudes();
        double minDistanceToNextCheckPoint = 9999999;
        double newDistanceToNextCheckPoint;
        int minIndexToNextCheckPoint = -1;
        int start;
        int end;
        double sumOfDistance;
        if(!route.getTemporary().get(index).equals("YES")) {
            for (int i = 0; i < routeLatitudes.size(); i++) {
                if(!route.getActive().get(i).equals("ABSENT")) {
                    newDistanceToNextCheckPoint = haverSineDistance(busLatitude, busLongitude, routeLatitudes.get(i), routeLongitudes.get(i));
                    if (newDistanceToNextCheckPoint < minDistanceToNextCheckPoint || i == 0) {
                        minDistanceToNextCheckPoint = newDistanceToNextCheckPoint;
                        minIndexToNextCheckPoint = i;
                    }
                }
            }
            if (minIndexToNextCheckPoint < index) {
                start = minIndexToNextCheckPoint;
                end = index;
            } else {
                start = index;
                end = minIndexToNextCheckPoint;
            }
            sumOfDistance = minDistanceToNextCheckPoint;
            for (int i = start; i < end; i++) {
                if(!route.getActive().get(i).equals("ABSENT")) {
                    sumOfDistance += haverSineDistance(routeLatitudes.get(i), routeLongitudes.get(i), routeLatitudes.get(i + 1), routeLongitudes.get(i + 1));
                }
            }
            return sumOfDistance / velocity;
        }
        else{
            return haverSineDistance(busLatitude, busLongitude, route.getLatitudes().get(index), route.getLongitudes().get(index)) / velocity;
        }
    }


}
