package Project.Persistent.SQL;

import Project.Mapper.BusMapper;
import Project.Model.Position.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class BusPersistent extends JdbcTemplate {

    @Autowired
    public BusPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public Bus getCurrentBusCarIdByStudentId(int personId) {
        Bus result;
        try {
            result = queryForObject("SELECT DISTINCT carId FROM PersonInBus WHERE atTime IN (SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?) " +
                    "AND PersonId = ? " +
                    "AND isInBus = 'YES' " +
                    "AND enterTime = (SELECT MAX(enterTime) FROM personInBus WHERE personId = ? AND atTime = (SELECT MAX(atTime) from PersonInBus Where personId = ?) )", new BusMapper(), personId, personId, personId, personId);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Bus getLatestBusCarIdByStudentId(int personId) {
        Bus result;
        try {
            result = queryForObject("SELECT DISTINCT carId FROM PersonInBus WHERE atTime IN (SELECT MAX(atTime) FROM PersonInBus WHERE personId = ?) " +
                    "AND PersonId = ? " +
                    "AND isInBus <> 'ABSENT' " +
                    "AND enterTime = (SELECT MAX(enterTime) FROM personInBus WHERE personId = ? AND atTime = (SELECT MAX(atTime) from PersonInBus Where personId = ?) )", new BusMapper(), personId, personId, personId, personId);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Bus getBusCarIdByStudentIdAndAtTime(int personId, Timestamp atTime) {
        Bus result;
        try {
            result = queryForObject("SELECT carId FROM PersonInBus where personId = ? AND enterTime = (SELECT MAX(enterTime) from personinbus WHERE atTime = ? and personId = ?) AND atTime = ?", new BusMapper(), personId, atTime, personId, atTime);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Bus getCurrentBusPosition(int carId) {
        Bus result;
        try {
            result = queryForObject("SELECT * FROM BusPosition WHERE atTime = " +
                            "(SELECT MAX(atTime) FROM BusPosition WHERE carId = ? )", (rs, rowNum) ->
                            new Bus(rs.getInt("carId"), rs.getDouble("latitude"), rs.getDouble("longitude"))
                    , carId);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public ArrayList<Bus> getAllBus() {
        return new ArrayList<>(query("SELECT * FROM BUS ORDER BY carId", (rs, rowNum) ->
                new Bus(rs.getInt("carId"), rs.getString("carNumber"), rs.getDouble("averageVelocity"), rs.getInt("checkPointPassed"))));
    }

    public boolean setVelocityToZero(int carId) {
        return update("UPDATE bus SET averageVelocity = 0, checkPointPassed = 0 WHERE carId = ? ", carId) == 1;
    }
    public boolean setVelocityAndCheckPointPassed(int carId, double averageVelocity, int checkPointPassed){
        return update("UPDATE bus SET averageVelocity = ?, checkPointPassed = ? WHERE carId = ? ", averageVelocity, checkPointPassed, carId) == 1;
    }

    public double getAverageVelocity(int carId){
        return queryForObject("SELECT averageVelocity FROM bus WHERE carId = ?", Double.class, carId);
    }

    public int getCheckPointPassed(int carId){
        return queryForObject("SELECT checkPointPassed FROM bus WHERE carId = ?", Integer.class, carId);
    }

    public String getBusCarNumberByCarId(int carId){
        try {
            return queryForObject("SELECT carNumber FROM bus WHERE carId = ?", String.class, carId);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }
}
