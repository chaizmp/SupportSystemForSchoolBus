package Project.PersistentLayer;

import Project.Mapper.BusMapper;
import Project.Model.Position.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/9/2559.
 */
@Service
public class BusPersistent extends JdbcTemplate{
    @Autowired
    TeacherPersistent teaPer;
    @Autowired
    DriverPersistent driPer;
    @Autowired
    public BusPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public Bus getBusByStudentId(String id)
    {
        Bus bus = null;
        List<Bus> busList = query("SELECT carNumber FROM BusAndStudent WHERE time = (SELECT MAX(time) FROM BusAndStudent WHERE studentId = '"+id+"')"
                ,new BusMapper()); // the latest time
        for(Bus it :busList)
        {
            if(it != null)
            {
                bus = it;
            }
        }
        if(bus != null)
        {
            String carNum = bus.getCarNumber();
            bus.setTeacher(teaPer.getTeacherByCarNum(carNum));
            bus.setDriver(driPer.getDriverByCarNum(carNum));
            //bus.setPosition(getPositionByCarNum(carNum));
            //bus.setVelocity(getVelocityFromCarNum(carNum));

        }
        return bus;
    }
    public ArrayList<Integer>[] getPositionByCarNum(String carNum)
    {
        return null;
    }

    public ArrayList<Float> getVelocityFromCarNum(String carNum)
    {
        return null;
    }




    public Bus getBusByTeacherId()
    {
        return null;
    }
}
