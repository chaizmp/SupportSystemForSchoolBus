package Project.Handler.Information;

import Project.Handler.Position.PositionHandler;
import Project.Model.Position.Bus;
import Project.Model.Position.Route;
import Project.Persistent.SQL.BusPersistent;
import Project.Persistent.SQL.StudentPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class BusHandler {

    @Autowired
    BusPersistent busPersistent;

    @Autowired
    StudentPersistent studentPersistent;

    @Autowired
    PositionHandler positionHandler;

    public Bus getCurrentBusCarIdByStudentId(int personId) {
        return busPersistent.getCurrentBusCarIdByStudentId(personId);
    }

    // A solution to get the bus trip that a student takes is
    // to make the bus log period subset of bus position
    // when the first person gets on, a record which has status 'START' will be inserted into the bus position table too
    // and when the last person get off, a record which has status 'FINISH' will be inserted also
    public Bus getBusCarIdByStudentIdAndAtTime(int personId, Timestamp atTime) {
        return busPersistent.getBusCarIdByStudentIdAndAtTime(personId, atTime);
    }

    public Bus getCurrentBusPosition(int carId) {
        return busPersistent.getCurrentBusPosition(carId);
    }

    public boolean setVelocityToZero(int carId) {
        return busPersistent.setVelocityToZero(carId);
    }

    public ArrayList<Bus> getAllBus() {
        return busPersistent.getAllBus();
    }

    public boolean setVelocityAndCheckPointPassed(int carId, double averageVelocity, int checkPointPassed){
        return busPersistent.setVelocityAndCheckPointPassed(carId, averageVelocity, checkPointPassed);
    }
    public double getAverageVelocity(int carId){
        return busPersistent.getAverageVelocity(carId);
    }
    public int getCheckPointPassed(int carId){
        return busPersistent.getCheckPointPassed(carId);
    }
    public Route getBusRoutinelyUsedRoute(int carId){
        return positionHandler.getBusRoutinelyUsedRoute(carId);
    }
    public String getBusCarNumberByCarId(int carId){
        return busPersistent.getBusCarNumberByCarId(carId);
    }
}

