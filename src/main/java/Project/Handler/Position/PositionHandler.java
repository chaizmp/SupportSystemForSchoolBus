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
import Project.Model.School;
import Project.Persistent.SQL.PositionPersistent;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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
    @Autowired
    SchoolHandler schoolHandler;

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

    public boolean addBusPosition(int carId, double latitude, double longitude, Status status, Timestamp timestamp) {
        return positionPersistent.addBusPosition(carId, latitude, longitude, status, timestamp);
    }


    public Integer addRoute(ArrayList<Double> latitudes, ArrayList<Double> longitudes, Type type, ArrayList<String> active, ArrayList<Integer> personIds, ArrayList<String> temporary) {
        School school = schoolHandler.getSchoolDetail("KMITL");
        if(type == Type.SCHOOL){
            latitudes.add(school.getLatitude());
            longitudes.add(school.getLongitude());
            active.add("YES");
            personIds.add(-2);
            temporary.add("NO");
        }
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

    public IsInBus getOnOrOffBus(int carId, int personId, Double latitude, Double longitude, boolean isStudent, IsInBus inBus) {

        // don't forget that the bus driver isn't the last person. we count only the students.
        IsInBus isInBus = positionPersistent.isInBus(personId);
        String onOrOff = "OFF";
        if (isInBus == IsInBus.NO ) {
            onOrOff = "ON";
            isInBus = IsInBus.YES;
        } else if(isInBus == IsInBus.YES) {
            isInBus = IsInBus.NO;
        }
        else{
            isInBus = IsInBus.YES;
        }
        if(inBus != null && inBus == IsInBus.TEMP && isInBus == IsInBus.NO){
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
            positionPersistent.addBusPosition(carId, latitude, longitude, status, null);
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
            positionPersistent.addBusPosition(carId, latitude, longitude, status, null);
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
        return isInBus;
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

    public double setVelocity(int carId, double previousLatitude, double previousLongitude, long previousTimestamp, double newLatitude, double newLongitude, long newTimestamp) {
        double oldAverageVelocity = busHandler.getAverageVelocity(carId);
        int checkPointPassed = busHandler.getCheckPointPassed(carId)+1;
        double averageVelocity = getAverageVelocity(oldAverageVelocity,checkPointPassed, previousLatitude, previousLongitude, previousTimestamp, newLatitude, newLongitude, newTimestamp);
        busHandler.setVelocityAndCheckPointPassed(carId, averageVelocity, checkPointPassed);
        return averageVelocity;
    }

    public double getAverageVelocity(double oldAverageVelocity, int checkPointPassed, double previousLatitude, double previousLongitude, long previousTimestamp, double newLatitude, double newLongitude, long newTimestamp){
        return (oldAverageVelocity*(checkPointPassed-1)+(haverSineDistance(previousLatitude, previousLongitude, newLatitude, newLongitude)/((newTimestamp-previousTimestamp)/1000)))/checkPointPassed;
    }

    public double haverSineDistance(double previousLatitude, double previousLongitude, double currentLatitude, double currentLongitude){
        double earthRadius = 6378137;
        double deltaLat = degreeToRadiant(previousLatitude-currentLatitude);
        double deltaLon = degreeToRadiant(previousLongitude-currentLongitude);
        double a = Math.sin(deltaLat/2)* Math.sin(deltaLat/2)+Math.cos(degreeToRadiant(previousLatitude))*Math.cos(degreeToRadiant(currentLatitude))*Math.sin(deltaLon/2)*Math.sin(deltaLon/2);
        double c =2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = earthRadius*c;
        return d;
    }

    public double degreeToRadiant(double degree){
        return degree * (Math.PI/180);
    }

    public Route getBusRoutinelyUsedRoute(int carId){
        SqlRowSet sqlRowSet = positionPersistent.getBusRouteByCarId(carId);
        Route route = new Route();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> actives = new ArrayList<>();
        ArrayList<Integer> personIds = new ArrayList<>();
        ArrayList<String> temporaries = new ArrayList<>();
        try {
            while (sqlRowSet.next()) {
                if (sqlRowSet.isFirst()) {
                    route.setRouteNumber(sqlRowSet.getInt("routeNumber"));
                    route.setType(Type.valueOf(sqlRowSet.getString("type")));
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
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //refactor after paper submission
    public String estimateTime(int carId, double velocity, Route route, int index, double busLatitude, double busLongitude){
        ArrayList<Double> routeLatitudes = route.getLatitudes();
        ArrayList<Double> routeLongitudes = route.getLongitudes();
        double minDistanceToNextCheckPoint = 9999999;
        //double newDistanceToNextCheckPoint;
        int minIndexToNextCheckPoint = -1;
        int start;
        int end;
        ArrayList<Student> students = studentHandler.getAllStudentInCurrentTrip(carId, false);
        Iterator<Student> itr = students.iterator();
        if(route.getType() == Type.SCHOOL) {
            while (itr.hasNext()) {
                Student stu = itr.next();
                if (stu.getInBus() != IsInBus.ABSENT) {
                    itr.remove();
                }
            }
        }else{
            while (itr.hasNext()) {
                Student stu = itr.next();
                if (stu.getInBus() != IsInBus.NO) {
                    itr.remove();
                }
            }
        }
        double sumOfDistance;
        if(!route.getTemporary().get(index).equals("YES")) {
            for (int i = 0; i <= index; i++) {
                if(!route.getActive().get(i).equals("ABSENT")) {
                    if(students.size() != 0) {
                        for (int j = 0; j< students.size(); j++) {
                            if (students.get(j).getId() == route.getPersonIds().get(i)) {
                                //minDistanceToNextCheckPoint = haverSineDistance(busLatitude, busLongitude, routeLatitudes.get(i), routeLongitudes.get(i));
                                minIndexToNextCheckPoint = i;
                                i = routeLatitudes.size();
                                j = students.size();
                                int previousStudentHomeIndex = getPreviousStudentHomeIndex(minIndexToNextCheckPoint, route);

                                double min = 0;
                                double dist;
                                int minIndex = -1;
                                for(int k = previousStudentHomeIndex; k<= minIndexToNextCheckPoint; k++){
                                    dist = haverSineDistance(busLatitude, busLongitude, route.getLatitudes().get(k), route.getLongitudes().get(k));
                                    if(dist < min || k == previousStudentHomeIndex){
                                        min = dist;
                                        minIndex = k;
                                    }
                                }
                                minDistanceToNextCheckPoint = min;
                                minIndexToNextCheckPoint = minIndex;

                            }
                        }
                    }
                    else {
                        minDistanceToNextCheckPoint = haverSineDistance(busLatitude, busLongitude, routeLatitudes.get(routeLatitudes.size()-1), routeLongitudes.get(routeLatitudes.size()-1));
                        minIndexToNextCheckPoint = routeLatitudes.size()-1;
                        i = routeLatitudes.size();
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
            JSONObject result = new JSONObject();
            result.put("time", sumOfDistance/velocity);
            result.put("distance", sumOfDistance);
            ///return sumOfDistance / velocity;
            return result.toString();
        }
        else{
            JSONObject result = new JSONObject();
            double dist = haverSineDistance(busLatitude, busLongitude, route.getLatitudes().get(index), route.getLongitudes().get(index));
            result.put("time", dist/velocity);
            result.put("distance", dist);
            return result.toString();
            //return haverSineDistance(busLatitude, busLongitude, route.getLatitudes().get(index), route.getLongitudes().get(index)) / velocity;
        }
    }

    public boolean clearTrip(){
        return positionPersistent.clearTrip();
    }

    public int getPreviousStudentHomeIndex(int latestIndex, Route route){
        for(int i = latestIndex - 1; i>0; i--){
            if(route.getActive().get(i).equals("YES")){
                return i;
            }
        }
        return latestIndex;
    }
    public boolean everInBus(int personId){
        return positionPersistent.everInBus(personId);
    }

    public boolean deleteFromRoute(int personId){
        return positionPersistent.deleteFromRoute(personId);
    }

    public double calculateAverageVelocity(int carId){
        ArrayList<JSONObject> busPositions = positionPersistent.getAllBusPositions(carId);
        int i;
        double sumOfVelocity = 0;
        for(i = 0; i<busPositions.size()-1;i++ ){
            sumOfVelocity += haverSineDistance(busPositions.get(i).getDouble("latitude"),
                    busPositions.get(i).getDouble("longitude"), busPositions.get(i+1).getDouble("latitude"),
                    busPositions.get(i+1).getDouble("longitude"))/((busPositions.get(i+1).getLong("time")-busPositions.get(i).getLong("time"))/1000);
        }
        return sumOfVelocity/i;
    }
}
