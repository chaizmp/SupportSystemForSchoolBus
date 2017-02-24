package Project.Handler.Information;

import Project.Model.Position.Bus;
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

    public Bus getCurrentBusCarNumberByStudentId(String personId)
    {
        return busPersistent.getCurrentBusCarNumberByStudentId(personId);
    }
    // A solution to get the bus trip that a student takes is
    // to make the bus log period subset of bus position
    // when the first person gets on, a record which has status 'START' will be inserted into the bus position table too
    // and when the last person get off, a record which has status 'FINISH' will be inserted also
    public Bus getBusCarNumberByStudentIdAndAtTime(String personId,Timestamp atTime){
        return busPersistent.getBusCarNumberByStudentIdAndAtTime(personId,atTime);
    }
    public Bus getCurrentBusPosition(String carNumber){
        return busPersistent.getCurrentBusPosition(carNumber);
    }

    public boolean setVelocityToZero(String carNumber){
        return busPersistent.setVelocityToZero(carNumber);
    }
    public ArrayList<Bus> getAllBus(){
        return busPersistent.getAllBus();
    }
}

