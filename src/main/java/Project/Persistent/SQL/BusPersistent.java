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

    public Bus getCurrentBusCarNumberByStudentId(String personId) {
        Bus result;
        try {
            result = queryForObject("SELECT carNumber FROM PersonInBus where atTime = (SELECT MAX(atTime) FROM PersonInBus WHERE personId = ? LIMIT 1)", new BusMapper(), personId);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Bus getBusCarNumberByStudentIdAndAtTime(String personId, Timestamp atTime) {
        Bus result;
        try {
            result = queryForObject("SELECT carNumber FROM PersonInBus where personId = ? AND atTime = ?", new BusMapper(), personId, atTime);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Bus getCurrentBusPosition(String carNumber) {
        Bus result;
        try {
            result = queryForObject("SELECT * FROM BusPosition WHERE atTime = " +
                            "(SELECT MAX(atTime) FROM BusPosition WHERE carNumber = ? )", (rs, rowNum) ->
                            new Bus(rs.getString("carNumber"), rs.getDouble("latitude"), rs.getDouble("longitude"))
                    , carNumber);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public ArrayList<Bus> getAllBus() {
        return new ArrayList<>(query("SELECT * FROM BUS ORDER BY carNumber", (rs, rowNum) ->
                new Bus(rs.getString("carNumber"), rs.getDouble("averageVelocity"), rs.getInt("checkPointPassed"))));
    }

    public boolean setVelocityToZero(String carNumber){
        return update("UPDATE bus SET averageVelocity = 0, checkPointPassed = 0 WHERE carNumber = ?", carNumber) == 1;
    }
}
