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

    public Driver getCurrentDriverInBusByCarNumber(String carNumber, ArrayList<Timestamp> startAndEndPeriod) {
        List<Driver> driverList;
        if (startAndEndPeriod.get(1) != null) {
            driverList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'DRIVER' " +
                    "AND atTime >=  ? AND atTime <= ? " +
                    "AND carNumber = ? ", new DriverMapper(), startAndEndPeriod.get(0), startAndEndPeriod.get(1), carNumber);
        } else {
            driverList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'DRIVER' " +
                    "AND atTime >= ? " +
                    "AND carNumber = ?", new DriverMapper(), startAndEndPeriod.get(0), carNumber);
        }
        if (driverList != null && driverList.size() != 0) {
            return driverList.get(0);
        }
        return null;
    }

}
