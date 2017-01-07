package Project.Persistent.SQL;

import Project.Mapper.PositionMapper;
import Project.Model.Enumerator.Status;
import Project.Model.Position.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/1/2560.
 */
@Service
public class PositionPersistent extends JdbcTemplate {
    @Autowired
    public PositionPersistent(DataSource mainDataSource){
        super();
        this.setDataSource(mainDataSource);
    }

    public Timestamp getLatestAtTimeByStudentId(String personId){
        Timestamp atTime;
        try {
            atTime = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE personId = ? LIMIT 1", Timestamp.class,personId);
        } catch (Exception e){
            atTime = null;
        }
        return atTime;
    }

    public Timestamp getTripStartTimeInPersonInBus(String carNumber, Timestamp atTime){
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE atTime <= ? "+
                    "AND STATUS = 'START' " +
                    "AND carNumber = ?", Timestamp.class, atTime,carNumber);
        }catch (Exception e){
            start = null;
        }
        return start;
    }

    public Timestamp getTripStartTimeInBusPosition(String carNumber,Timestamp atTime){
        Timestamp start;
        try {
            start = queryForObject("SELECT MAX(atTime) FROM BusPosition " +
                    "WHERE atTime <= ? "+
                    "AND STATUS = 'PERSONSTART' " +
                    "AND carNumber = ?", Timestamp.class, atTime,carNumber);
        }catch (Exception e){
            start = null;
        }
        return start;
    }

    public Timestamp getTripFinishTimeInPersonInBus(String carNumber,Timestamp atTime){

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM PersonInBus " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carNumber = ? ", Timestamp.class, atTime,carNumber);
        }catch (Exception e){
            end = null;
        }
        return end;
    }
    public Timestamp getTripFinishTimeInBusPosition(String carNumber,Timestamp atTime){

        Timestamp end;
        try {
            end = queryForObject("SELECT MIN(atTime) FROM BusPosition " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH' " +
                    "AND carNumber = ? ", Timestamp.class, atTime,carNumber);
        }catch (Exception e){
            end = null;
        }
        return end;
    }
    public ArrayList<Position> getActualRouteInBusByPeriod(String carNumber,Timestamp startTime,Timestamp endTime){
        List<Position> positionList;
        if(endTime != null) {
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >=  ? AND atTime <= ? " +
                    "AND carNumber = ? ", new PositionMapper(),startTime,endTime, carNumber);
        }
        else{
            positionList = query("SELECT * FROM BusPosition WHERE " +
                    "atTime >= ? " +
                    "AND carNumber = ?", new PositionMapper(),startTime, carNumber);
        }
        return new ArrayList<>(positionList);
    }

    public boolean addBusPosition(String carNumber, float latitude, float longitude, Status status){
        try {
            return update("INSERT INTO busPosition(carNumber,latitude,longitude,status) VALUES (?,?,?,?)", carNumber, latitude, longitude,
                    status.toString()) > 0;
        }catch (Exception e){
            return false;
        }
    }

    public boolean addRoute(ArrayList<Float> latitudes, ArrayList<Float> longitudes){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        update( connection ->
                connection.prepareStatement("INSERT INTO ROUTE(routeNumber) VALUES(null) ", new String[] {"id"}),
                keyHolder);
        String routeNumber = keyHolder.getKey().toString();
        int result = 0;
        int i;
        for(i = 0; i< latitudes.size(); i++) {
            result += update("INSERT INTO RoutePosition(routeNumber,sequenceNumber,latitude,longitude) VALUES (?,?,?,?) ",routeNumber,i,latitudes.get(i)
            ,longitudes.get(i));
        }
        return result == i;
    }
}
