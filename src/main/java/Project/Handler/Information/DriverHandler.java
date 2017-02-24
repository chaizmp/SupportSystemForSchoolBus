package Project.Handler.Information;

import Project.Model.Person.Driver;
import Project.Persistent.SQL.DriverPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by User on 3/1/2560.
 */
@Service
public class DriverHandler {
    @Autowired
    DriverPersistent driverPersistent;

    public Driver getCurrentDriverInBusByCarNumber(String carNumber, ArrayList<Timestamp> startAndEndPeriod) {
        return driverPersistent.getCurrentDriverInBusByCarNumber(carNumber, startAndEndPeriod);
    }
}
