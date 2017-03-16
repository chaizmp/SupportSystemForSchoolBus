package Project.Persistent.SQL;

import Project.Mapper.DriverMapper;
import Project.Model.Person.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/1/2560.
 */
@Service
public class DriverPersistent extends JdbcTemplate {

    @Autowired
    public DriverPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public Driver getCurrentDriverInBusByCarId(int carId, ArrayList<Timestamp> startAndEndPeriod) {
        List<Driver> driverList;
        if (startAndEndPeriod.get(1) != null) {
            driverList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'DRIVER' " +
                    "AND atTime >=  ? AND atTime <= ? " +
                    "AND carId = ? ", new DriverMapper(), startAndEndPeriod.get(0), startAndEndPeriod.get(1), carId);
        } else {
            driverList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'DRIVER' " +
                    "AND atTime >= ? " +
                    "AND carId = ?", new DriverMapper(), startAndEndPeriod.get(0), carId);
        }
        if (driverList != null && driverList.size() != 0) {
            return driverList.get(0);
        }
        return null;
    }

    public Driver getLatestDriverInBusByCarId(int carId){
        try {
            return queryForObject("SELECT * FROM PersonInBus, Person WHERE Person.personId = personInBus.personId " +
                    "AND Person.role = 'DRIVER' " +
                    "AND carId = ? " +
                    "AND atTime >= ( SELECT MAX(atTime) FROM personInBus WHERE carId = ? AND status = 'START' )", new DriverMapper(), carId, carId);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
