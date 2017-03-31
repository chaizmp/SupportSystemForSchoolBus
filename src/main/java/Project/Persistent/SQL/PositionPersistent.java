package Project.Persistent.SQL;

import Project.Mapper.PositionMapper;
import Project.Model.Enumerator.*;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/1/2560.
 */
@Service
public class PositionPersistent extends JdbcTemplate {
    @Autowired
    public PositionPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public Timestamp getLatestAtTimeByStudentId(int personId) {
        Timestamp atTime;
        try {
            atTime = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE personId = ?", Timestamp.class, personId);
        } catch (Exception e) {
            atTime = null;
        }
        return atTime;
    }

    public Timestamp getTripStartTimeInPersonInBus(int carId, Timestamp atTime) {
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE atTime <= ? " +
                    "AND STATUS = 'START' " +
                    "AND carId = ?", Timestamp.class, atTime, carId);
        } catch (Exception e) {
            start = null;
        }
        return start;
    }

    public Timestamp getTripStartTimeInBusPosition(int carId, Timestamp atTime) {
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM BusPosition " +
                    "WHERE atTime <= ? " +
                    "AND STATUS = 'PERSONSTART' " +
                    "AND carId = ?", Timestamp.class, atTime, carId);
        } catch (Exception e) {
            start = null;
        }
        return start;
    }

    public Timestamp getTripFinishTimeInPersonInBus(int carId, Timestamp atTime) {

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM PersonInBus " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carId = ? ", Timestamp.class, atTime, carId);
        } catch (Exception e) {
            end = null;
        }
        return end;
    }

    public Timestamp getTripFinishTimeInBusPosition(int carId, Timestamp atTime) {

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM BusPosition " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carId = ? ", Timestamp.class, atTime, carId);
        } catch (Exception e) {
            end = null;
        }
        return end;
    }

    public ArrayList<Position> getActualRouteInBusByPeriod(int carId, Timestamp startTime, Timestamp endTime) {
        List<Position> positionList;
        if (endTime != null) {
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >=  ? AND atTime <= ? " +
                    "AND carId = ? ORDER by AtTime ASC", new PositionMapper(), startTime, endTime, carId);
        } else {
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >= ? " +
                    "AND carId = ? ORDER by AtTime ASC", new PositionMapper(), startTime, carId);
        }
        return new ArrayList<>(positionList);
    }

    public boolean addBusPosition(int carId, double latitude, double longitude, Status status, Timestamp timestamp) {
        if(timestamp != null) {
            try {
                return update("INSERT INTO busPosition(carId,latitude,longitude,status,atTime) VALUES (?,?,?,?,?)", carId, latitude, longitude,
                        status.toString(), timestamp) > 0;
            } catch (Exception e) {
                return false;
            }
        }else{
            try {
                return update("INSERT INTO busPosition(carId,latitude,longitude,status) VALUES (?,?,?,?)", carId, latitude, longitude,
                        status.toString()) > 0;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Integer addRoute(ArrayList<Double> latitudes, ArrayList<Double> longitudes, Type type, ArrayList<String> active, ArrayList<Integer> personIds, ArrayList<String> temporary) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        update(connection ->
                        connection.prepareStatement("INSERT INTO ROUTE(type) VALUES ( '"+type.name()+"') ", new String[]{"id"}),
                keyHolder);
        Integer routeNumber = keyHolder.getKey().intValue();
        int result = 0;
        int i;
        for (i = 0; i < latitudes.size(); i++) {
            result += update("INSERT INTO RoutePosition(routeNumber,sequenceNumber,latitude,longitude,active,personId, temporary) VALUES (?,?,?,?,?,?,?) ", routeNumber, i, latitudes.get(i)
                    , longitudes.get(i), active.get(i), personIds.get(i), temporary.get(i));
        }
        return result == i ? routeNumber : -1;
    }

    public boolean addTemporaryRoute(int routeNumber, double latitude, double longitude, String active, int personId, String temporary) {

        List<Integer> sequenceNumbers = queryForList("SELECT sequenceNumber FROM routePosition WHERE routeNumber = ?", Integer.class, routeNumber);
        try {
            update("UPDATE routePosition SET active = 'NO' WHERE routeNumber = ? and personId = ?", routeNumber, personId);
            update("DELETE FROM routePosition WHERE routeNumber = ? and personId = ? AND temporary = 'YES'", routeNumber, personId);
            boolean result = update("INSERT INTO RoutePosition(routeNumber,sequenceNumber,latitude,longitude,active,personId, temporary) VALUES (?,?,?,?,?,?,?) ", routeNumber, sequenceNumbers.size(), latitude
                    , longitude, active, personId, temporary) == 1;
            return result;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoute(int routeNumber) {
        update("DELETE FROM routePosition WHERE routeNumber = ?", routeNumber);
        return update("DELETE FROM route WHERE routeNumber = ?", routeNumber) == 1;
    }

    public boolean setBusRoute(int carId, int routeNumber) {
        if(routeNumber != -1) {
            return update("INSERT INTO busRoute(carId,RouteNumber) VALUES(?,?)", carId, routeNumber) == 1;
        }else{
            return update("INSERT INTO busRoute(carId,RouteNumber) VALUES(?,?)", carId, null) == 1;
        }
    }

    public IsInBus isInBus(int personId) {
        IsInBus result;
        try {
            result = IsInBus.valueOf(queryForObject("SELECT isInBus FROM PersonInBus WHERE enterTime = (SELECT MAX(enterTime) FROM PersonInBus WHERE personId = ? " +
                    "AND atTime = ( SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?))" +
                    "AND atTime = (" +
                    "SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?) ", String.class, personId, personId, personId));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return IsInBus.NO;
        }
    }

    public IsInBus isInBusPeriod(int personId, Timestamp start, Timestamp stop) {
        IsInBus result;
        try {
            if(stop != null) {
                result = IsInBus.valueOf(queryForObject("SELECT isInBus FROM PersonInBus WHERE enterTime = (SELECT MAX(enterTime) FROM PersonInBus WHERE personId = ? " +
                        "AND atTime = ( SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?)) " +
                        "AND atTime >= ? " +
                        "AND atTime <= ? " +
                        "AND atTime = (" +
                        "SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?) ", String.class, personId, personId, start, stop, personId));
            }
            else {
                result = IsInBus.valueOf(queryForObject("SELECT isInBus FROM PersonInBus WHERE enterTime = (SELECT MAX(enterTime) FROM PersonInBus WHERE personId = ? " +
                        "AND atTime = ( SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?)) " +
                        "AND atTime >= ? " +
                        "AND atTime = (" +
                        "SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?) ", String.class, personId, personId, start, personId));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return IsInBus.ABSENT;
        }
    }

    public SqlRowSet getAllBusRoute() {
        return queryForRowSet("SELECT * FROM route,routePosition WHERE route.routeNumber = routePosition.routeNumber ORDER BY routePosition.routeNumber, routePosition.sequenceNumber ASC");
    }

    public SqlRowSet getBusRouteByCarId(int carId) {
        return queryForRowSet("SELECT * FROM routePosition,busRoute,route WHERE busRoute.routeNumber = route.routeNumber " +
                "AND busRoute.routeNumber = routePosition.routeNumber " +
                "AND carId = ? " +
                "AND busRoute.useTime = ( SELECT MAX(useTime) from busRoute WHERE carId = ? ) ORDER BY routePosition.routeNumber, routePosition.sequenceNumber ASC", carId, carId);
    }

    public Position getCurrentBusPosition(int carId) {
        Position position;
        try {
            position = queryForObject("SELECT latitude,longitude FROM busPosition WHERE carId = ? AND AtTime = (" +
                    "SELECT MAX(AtTime) FROM busPosition WHERE carId = ?)", (rs, rowNum) ->
                    new Position(rs.getDouble("latitude"), rs.getDouble("longitude")), carId, carId);
            return position;
        } catch (Exception e) {
            e.printStackTrace();
            return new Position(200, 200);
        }
    }

    public boolean  isFirstPerson(int carId, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
            return !queryForRowSet("SELECT * FROM personinbus WHERE STATUS = ? AND carId = ? AND atTime <= ? AND atTime >= ? ", Status.START.name(), carId, lunch, midNight).next();
        } else {
            return !queryForRowSet("SELECT * FROM personinbus WHERE STATUS = ? AND carId = ? AND atTime > ? AND atTime >= ? ", Status.START.name(), carId, lunch, midNight).next();
        }
    }

    public boolean isLastPerson(int personId, int carId, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
            return !queryForRowSet("SELECT COUNT(*) FROM PersonInBus P1 WHERE carId = ? " +
                    "AND isInBus = ? " +
                    "AND atTime <= ? " +
                    "AND atTime >= ? " +
                    "AND personId NOT IN (SELECT personId FROM person WHERE personId = ? OR role = ?) " +
                    "AND COUNT(*) -1 = (SELECT COUNT(*)-1 FROM student WHERE typeOfService = ? OR typeOfService = ?)", carId, IsInBus.NO.name(), lunch, midNight, personId, Role.DRIVER.name(), TypeOfService.BOTH.name(), TypeOfService.GO.name()).next();
        } else
            return !queryForRowSet("SELECT * FROM PersonInBus P1 " +
                    "HAVING COUNT(*) -1 = (SELECT COUNT(*) - 1 FROM student WHERE typeOfService = ? OR typeOfService = ?)" +
                    "AND carId = ? " +
                    "AND isInBus = ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND personId NOT IN (SELECT personId FROM person WHERE personId = ? OR role = ?)", carId, IsInBus.NO.name(), lunch, midNight, personId, Role.DRIVER.name(), TypeOfService.BOTH.name(), TypeOfService.BACK.name()).next();
    }

    public boolean getOnOrOffBus(int carId, int personId, Double latitude, Double longitude, IsInBus isInBus, Status status, int enterTime) {
        return update("INSERT INTO PersonInBus(personId, carId, isInBus, latitude, longitude, status, enterTime) VALUES(?,?,?,?,?,?,?)", personId, carId, isInBus.name(), latitude, longitude, status.name(), enterTime) == 1;
    }

    public int latestEnterTime(int personId, int carId /*, Timestamp now, Timestamp lunch, Timestamp midNight */) {
       /* if (now.getTime() <= lunch.getTime()) {
            try {
                return queryForObject("SELECT enterTime from personInBus WHERE " +
                        "personId = ? " +
                        "AND atTime = ( SELECT MAX(atTime) FROM personInBus WHERE carNumber = ? " +
                        "AND atTime >= ? " +
                        "AND atTime <= ? " +
                        "AND personId = ? )", Integer.class, personId, carNumber, midNight, lunch, personId);
            } catch (Exception e) {
                return -1;
            }
        } else {
            try {
                return queryForObject("SELECT enterTime from personInBus WHERE " +
                        "personId = ? " +
                        "AND atTime = ( SELECT MAX(atTime) FROM personInBus WHERE carNumber = ? " +
                        "AND atTime >= ? " +
                        "AND atTime >= ? " +
                        "AND personId = ? )", Integer.class, personId, carNumber, midNight, lunch, personId);
            } catch (Exception e) {
                return -1;
            }
        }*/
       try {
           return queryForObject("SELECT MAX(enterTime) FROM personInBus WHERE " +
                   "personId= ? and carId= ?", Integer.class, personId, carId);
       }catch(Exception e){
           e.printStackTrace();
           return -1;
       }
    }
    public int getStudentsUsedToBeOnBusInCurrentTrip(int carId, Timestamp now, Timestamp lunch, Timestamp midNight){
        if(now.getTime() >= lunch.getTime()) {
            return queryForObject("SELECT COUNT(DISTINCT personId) FROM personInBus WHERE carId = ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND isInBus = 'YES' ", Integer.class, carId, lunch, midNight);
        }else{
            return queryForObject("SELECT COUNT(DISTINCT personId) FROM personInBus WHERE carId = ? " +
                    "AND atTime < ? " +
                    "AND atTime >= ? " +
                    "AND isInBus = 'YES' ", Integer.class, carId, lunch, midNight);
        }
    }

    public List<Integer> getAllStudentsUsedToBeOnBusInCurrentTrip(int carId, Timestamp now, Timestamp lunch, Timestamp midNight){
        if(now.getTime() >= lunch.getTime()) {
            return queryForList("SELECT DISTINCT personId FROM personInBus WHERE carId = ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND isInBus = 'YES' ", Integer.class, carId, lunch, midNight);
        }else{
            return queryForList("SELECT DISTINCT personId FROM personInBus WHERE carId = ? " +
                    "AND atTime < ? " +
                    "AND atTime >= ? " +
                    "AND isInBus = 'YES' ", Integer.class, carId, lunch, midNight);
        }
    }
    public boolean deleteTemporary(Route route){
        int routeNumber = route.getRouteNumber();
        ArrayList<Integer> personIds = new ArrayList<>();
        for(int i = 0; i< route.getLatitudes().size(); i++){
            if(route.getTemporary().get(i).equals("YES")){
                personIds.add(route.getPersonIds().get(i));
            }
        }
        try {
            update("DELETE FROM RoutePosition WHERE routeNumber = ? AND temporary = 'YES' ", routeNumber);
            for (int it : personIds) {
                update("UPDATE routePosition SET active = 'YES' WHERE routeNumber= ? AND personId = ?", routeNumber, it);
            }
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearTrip(){
        boolean result1;
        boolean result2;
        boolean result3;
        boolean result4;
        String a = null;
        try{
            result1 = update("DELETE FROM personInBus") >= 1;
            result2 = update("DELETE FROM busPosition") >= 1;
            result3 = update("UPDATE student SET carId = ?", a) >= 1;
            result4 = update("UPDATE bus SET averageVelocity = 0 , checkPointPassed = 0 ") >= 1;
            return result1 && result2 && result3 && result4;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean everInBus(int personId){
        try {
            return queryForRowSet("SELECT * FROM personInBus WHERE personId = ?", personId).next();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFromRoute(int personId){
        try{
            return update("DELETE FROM routePosition WHERE personId = ?", personId)>=0;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
