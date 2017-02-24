package Project.Persistent.SQL;

import Project.Mapper.PositionMapper;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Position.Position;
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

    public Timestamp getLatestAtTimeByStudentId(String personId) {
        Timestamp atTime;
        try {
            atTime = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE personId = ? LIMIT 1", Timestamp.class, personId);
        } catch (Exception e) {
            atTime = null;
        }
        return atTime;
    }

    public Timestamp getTripStartTimeInPersonInBus(String carNumber, Timestamp atTime) {
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE atTime <= ? " +
                    "AND STATUS = 'START' " +
                    "AND carNumber = ?", Timestamp.class, atTime, carNumber);
        } catch (Exception e) {
            start = null;
        }
        return start;
    }

    public Timestamp getTripStartTimeInBusPosition(String carNumber, Timestamp atTime) {
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM BusPosition " +
                    "WHERE atTime <= ? " +
                    "AND STATUS = 'PERSONSTART' " +
                    "AND carNumber = ?", Timestamp.class, atTime, carNumber);
        } catch (Exception e) {
            start = null;
        }
        return start;
    }

    public Timestamp getTripFinishTimeInPersonInBus(String carNumber, Timestamp atTime) {

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM PersonInBus " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carNumber = ? ", Timestamp.class, atTime, carNumber);
        } catch (Exception e) {
            end = null;
        }
        return end;
    }

    public Timestamp getTripFinishTimeInBusPosition(String carNumber, Timestamp atTime) {

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM BusPosition " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carNumber = ? ", Timestamp.class, atTime, carNumber);
        } catch (Exception e) {
            end = null;
        }
        return end;
    }

    public ArrayList<Position> getActualRouteInBusByPeriod(String carNumber, Timestamp startTime, Timestamp endTime) {
        List<Position> positionList;
        if (endTime != null) {
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >=  ? AND atTime <= ? " +
                    "AND carNumber = ? ORDER by AtTime ASC", new PositionMapper(), startTime, endTime, carNumber);
        } else {
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >= ? " +
                    "AND carNumber = ? ORDER by AtTime ASC", new PositionMapper(), startTime, carNumber);
        }
        return new ArrayList<>(positionList);
    }

    public boolean addBusPosition(String carNumber, double latitude, double longitude, Status status) {
        try {
            return update("INSERT INTO busPosition(carNumber,latitude,longitude,status) VALUES (?,?,?,?)", carNumber, latitude, longitude,
                    status.toString()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer addRoute(ArrayList<Double> latitudes, ArrayList<Double> longitudes) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        update(connection ->
                        connection.prepareStatement("INSERT INTO ROUTE(routeNumber) VALUES(null) ", new String[]{"id"}),
                keyHolder);
        Integer routeNumber = keyHolder.getKey().intValue();
        int result = 0;
        int i;
        for (i = 0; i < latitudes.size(); i++) {
            result += update("INSERT INTO RoutePosition(routeNumber,sequenceNumber,latitude,longitude) VALUES (?,?,?,?) ", routeNumber, i, latitudes.get(i)
                    , longitudes.get(i));
        }
        return result == i ? routeNumber : -1;
    }

    public boolean deleteRoute(int routeNumber) {
        update("DELETE FROM routePosition WHERE routeNumber = ?", routeNumber);
        return update("DELETE FROM route WHERE routeNumber = ?", routeNumber) == 1;
    }

    public boolean setBusRoute(String carNumber, int routeNumber) {
        return update("INSERT INTO busRoute(carNumber,RouteNumber) VALUES(?,?)", carNumber, routeNumber) == 1;
    }

    public IsInBus isInBus(String personId) {
        IsInBus result;
        try {
            result = IsInBus.valueOf(queryForObject("SELECT isInBus FROM PersonInBus WHERE atTime = (" +
                    "SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?)", String.class, personId));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return IsInBus.NO;
        }
    }

    public SqlRowSet getAllBusRoute() {
        return queryForRowSet("SELECT * FROM routePosition ORDER BY routeNumber, sequenceNumber ASC");
    }

    public SqlRowSet getBusRouteByCarNumber(String carNumber) {
        return queryForRowSet("SELECT routePosition.routeNumber, routePosition.sequenceNumber, routePosition.latitude, routePosition.longitude FROM routePosition,busRoute WHERE busRoute.routeNumber = routePosition.routeNumber and carNumber = ? ORDER BY routePosition.routeNumber, routePosition.sequenceNumber ASC", carNumber);
    }

    public Position getCurrentBusPosition(String carNumber) {
        Position position;
        try {
            position = queryForObject("SELECT latitude,longitude FROM busPosition WHERE carNumber = ? AND AtTime = (" +
                    "SELECT MAX(AtTime) FROM busPosition WHERE carNumber = ?)", (rs, rowNum) ->
                    new Position(rs.getDouble("latitude"), rs.getDouble("longitude")), carNumber, carNumber);
            return position;
        } catch (Exception e) {
            e.printStackTrace();
            return new Position(200, 200);
        }
    }

    public boolean isFirstPerson(String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
            return !queryForRowSet("SELECT * FROM personinbus WHERE STATUS = ? AND carNumber = ? AND atTime <= ? AND atTime >= ? ", Status.START.name(), carNumber, lunch, midNight).next();
        } else {
            return !queryForRowSet("SELECT * FROM personinbus WHERE STATUS = ? AND carNumber = ? AND atTime > ? AND atTime >= ? ", Status.START.name(), carNumber, lunch, midNight).next();
        }
    }

    public boolean isLastPerson(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
            return !queryForRowSet("SELECT COUNT(*) FROM PersonInBus P1 WHERE carNumber = ? " +
                    "AND isInBus = ? " +
                    "AND atTime <= ? " +
                    "AND atTime >= ? " +
                    "AND personId NOT IN (SELECT personId FROM person WHERE personId = ? OR role = ?) " +
                    "AND COUNT(*) -1 = (SELECT COUNT(*)-1 FROM student WHERE typeOfService = ? OR typeOfService = ?)", carNumber, IsInBus.NO.name(), lunch, midNight, personId, Role.DRIVER.name(), TypeOfService.BOTH.name(), TypeOfService.GO.name()).next();
        } else
            return !queryForRowSet("SELECT * FROM PersonInBus P1 " +
                    "HAVING COUNT(*) -1 = (SELECT COUNT(*) - 1 FROM student WHERE typeOfService = ? OR typeOfService = ?)" +
                    "AND carNumber = ? " +
                    "AND isInBus = ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND personId NOT IN (SELECT personId FROM person WHERE personId = ? OR role = ?)", carNumber, IsInBus.NO.name(), lunch, midNight, personId, Role.DRIVER.name(), TypeOfService.BOTH.name(), TypeOfService.BACK.name()).next();
    }

    public boolean getOnOrOffBus(String carNumber, String personId, Double latitude, Double longitude, IsInBus isInBus, Status status, int enterTime) {
        return update("INSERT INTO PersonInBus(personId, carNumber, isInBus, latitude, longitude, status, enterTime) VALUES(?,?,?,?,?,?,?)", personId, carNumber, isInBus.name(), latitude, longitude, status.name(), enterTime) == 1;
    }

    public int latestEnterTime(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
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
        }

    }

}
