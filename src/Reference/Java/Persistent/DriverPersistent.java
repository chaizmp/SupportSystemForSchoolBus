package Project.PersistentLayer;

import Project.Mapper.DriverMapper;
import Project.Model.Person.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by User on 2/9/2559.
 */
@Service
public class DriverPersistent extends JdbcTemplate {
    @Autowired
    public DriverPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public Driver getDriverByCarNum(String carNum) {
        List<Driver> driList = query("SELECT * FROM Driver where driverId = (SELECT driverId FROM BusAndDriver WHERE time = (SELECT MAX(time) FROM BusAndDriver WHERE carNumber = '" + carNum + "'))"
                , new DriverMapper());
        Driver driver = null;
        for (Driver it : driList) {
            if (it != null) {
                driver = it;
            }
        }
        return driver;
    }
}
