package Project.Handler.Position;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.BusHandler;
import Project.Handler.Information.PersonHandler;
import Project.Handler.Information.StudentHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Notification.NotificationForm;
import Project.Model.Notification.NotificationMessage;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
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

;

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

    public ArrayList<Timestamp> getCurrentStartAndEndPeriodByStudentId(String carNumber, String personId) {
        ArrayList<Timestamp> period = new ArrayList<>();
        Timestamp atTime = positionPersistent.getLatestAtTimeByStudentId(personId);
        period.add(positionPersistent.getTripStartTimeInPersonInBus(carNumber, atTime));
        period.add(positionPersistent.getTripFinishTimeInPersonInBus(carNumber, atTime));
        return period;
    }

    public Timestamp getLatestAtTimeByStudentId(String personId) {
        return positionPersistent.getLatestAtTimeByStudentId(personId);
    }

    public ArrayList<Position> getActualRouteInTripByAtTime(String carNumber, Timestamp atTime) {
        Timestamp startTime = positionPersistent.getTripStartTimeInBusPosition(carNumber, atTime);
        Timestamp endTime = positionPersistent.getTripFinishTimeInBusPosition(carNumber, atTime);
        return positionPersistent.getActualRouteInBusByPeriod(carNumber, startTime, endTime);
    }

    public boolean addBusPosition(String carNumber, double latitude, double longitude, Status status) {
        return positionPersistent.addBusPosition(carNumber, latitude, longitude, status);
    }

    public Integer addRoute(ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
        return positionPersistent.addRoute(latitudes, longitudes);
    }

    public boolean deleteRoute(int routeNumber) {
        return positionPersistent.deleteRoute(routeNumber);
    }

    public boolean setBusRoute(String carNumber, int routeNumber) {
        return positionPersistent.setBusRoute(carNumber, routeNumber);
    }

    public ArrayList<Route> getAllBusRoute() {
        SqlRowSet sqlRowSet = positionPersistent.getAllBusRoute();
        ArrayList<Route> routeList = new ArrayList<>();
        int current = -1;
        int next;
        Route route = new Route();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        while (sqlRowSet.next()) {
            next = sqlRowSet.getInt("routeNumber");
            if (current != next) {
                if (current != -1) {
                    routeList.add(route);
                    route.setLatitudes(latitudes);
                    route.setLongitudes(longitudes);
                }
                current = next;
                route = new Route();
                latitudes = new ArrayList<>();
                longitudes = new ArrayList<>();
                route.setRouteNumber(current);
            }
            latitudes.add(sqlRowSet.getDouble("latitude"));
            longitudes.add(sqlRowSet.getDouble("longitude"));
        }
        route.setLatitudes(latitudes);
        route.setLongitudes(longitudes);
        routeList.add(route);
        return routeList;
    }

    public ArrayList<Bus> getAllCurrentBusPosition() {
        //ArrayList<Bus> busList = busPersistent.getAllBus();
        ArrayList<Bus> busList = busHandler.getAllBus();
        for (Bus it : busList) {
            SqlRowSet sqlRowSet = positionPersistent.getBusRouteByCarNumber(it.getCarNumber());
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
            Position position = positionPersistent.getCurrentBusPosition(it.getCarNumber());
            it.setCurrentLatitude(position.getLatitude());
            it.setCurrentLongitude(position.getLongitude());
        }
        return busList;
    }

    public boolean getOnOrOffBus(String carNumber, String personId, Double latitude, Double longitude) {

        // don't forget that bus driver isn't the last person. we count only the students.
        IsInBus isInBus = positionPersistent.isInBus(personId);
        String onOrOff = "OFF";
        if (isInBus == IsInBus.NO) {
            onOrOff = "ON";
            isInBus = IsInBus.YES;
        } else {
            isInBus = IsInBus.NO;
        }
        Student student = studentHandler.getStudentByPersonId(personId);
        ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId);
        String studentName = student.getFirstName();
        String surname = student.getSurName();
        Status status;
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 12); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Timestamp time = new Timestamp(c.getTimeInMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp midNight = new Timestamp(c.getTimeInMillis());
        if (positionPersistent.isFirstPerson(carNumber, now, time, midNight)) {
            status = Status.PERSONSTART;
            positionPersistent.addBusPosition(carNumber, latitude, longitude, status);
            status = Status.START;
            // can be the driver
        } else if (isInBus == IsInBus.NO && isLastPerson(personId, carNumber, now, time, midNight)) {
            status = Status.FINISH;
            positionPersistent.addBusPosition(carNumber, latitude, longitude, status);
            busHandler.setVelocityToZero(carNumber);
            // the last student not the driver
        } else {
            status = Status.NONE;
        }
        int enterTime = positionPersistent.latestEnterTime(personId, carNumber, now, time, midNight) + 1;
        positionPersistent.getOnOrOffBus(carNumber, personId, latitude, longitude, isInBus, status, enterTime);
        for (Person it : userRelatedToStudent) {
            NotificationMessage notificationMessage = new NotificationMessage();
            NotificationForm notificationForm = new NotificationForm();
            notificationForm.setTitle("Support System For School Bus");
            notificationForm.setBody("Your student, " + studentName + " " + surname + " " + ", has just get " + onOrOff + "The Bus Number" + carNumber);
            notificationMessage.setNotificationForm(notificationForm);
            notificationMessage.setTo(it.getToken());
            apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);
        }
        return isInBus != IsInBus.YES;
    }

    public boolean isLastPerson(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight) {
        TypeOfService typeOfService;
        if (now.getTime() >= lunch.getTime()) {
            typeOfService = TypeOfService.BACK;
        } else {
            typeOfService = TypeOfService.GO;
        }
        int studentNumber = studentHandler.getNumberOfStudentInCurrentTrip(typeOfService);
        int studentNotInCarNumber = studentHandler.getNumberOfStudentGetOutInCurrentTripExceptPersonId(personId, carNumber, now, lunch, midNight);
        return studentNumber - 1 == studentNotInCarNumber;
    }

    public void setVelocity(String carNumber, double previousLatitude, double previousLongitude, double newLatitude, double newLongitude) {
        double oldAverageVelocity = busHandler.getAverageVelocity(carNumber);
        int checkPointPassed = busHandler.getCheckPointPassed(carNumber);
        double averageVelocity = getAverageVelocity(oldAverageVelocity,checkPointPassed, previousLatitude, previousLongitude, newLatitude, newLongitude);
        busHandler.setVelocity(carNumber, averageVelocity, checkPointPassed);
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
}
