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

    public Driver getCurrentDriverInBusByCarId(int carId, ArrayList<Timestamp> startAndEndPeriod) {
        return driverPersistent.getCurrentDriverInBusByCarId(carId, startAndEndPeriod);
    }

    public Driver getLatestDriverInBusByCarId(int carId){
        return driverPersistent.getLatestDriverInBusByCarId(carId);
    }
}
