package Project.Handler.Position;

import Project.Model.Enumerator.Status;
import Project.Model.Position.Position;
import Project.Persistent.SQL.PositionPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by User on 6/1/2560.
 */
@Service
public class PositionHandler {

    @Autowired
    PositionPersistent positionPersistent;

    public ArrayList<Timestamp> getCurrentStartAndEndPeriodByStudentId(String carNumber,String personId){
        ArrayList<Timestamp> period = new ArrayList<>();
        Timestamp atTime = positionPersistent.getLatestAtTimeByStudentId(personId);
        period.add(positionPersistent.getTripStartTimeInPersonInBus(carNumber,atTime));
        period.add(positionPersistent.getTripFinishTimeInPersonInBus(carNumber,atTime));
        return period;
    }

    public Timestamp getLatestAtTimeByStudentId(String personId){
        return positionPersistent.getLatestAtTimeByStudentId(personId);
    }

    public ArrayList<Position> getActualRouteInTripByAtTime(String carNumber,Timestamp atTime){
        Timestamp startTime =  positionPersistent.getTripStartTimeInBusPosition(carNumber,atTime);
        Timestamp endTime = positionPersistent.getTripFinishTimeInBusPosition(carNumber,atTime);
        return positionPersistent.getActualRouteInBusByPeriod(carNumber,startTime,endTime);
    }

    public boolean addBusPosition(String carNumber, float latitude, float longitude, Status status){
        return positionPersistent.addBusPosition(carNumber,latitude,longitude,status);
    }

    public boolean addRoute(ArrayList<Float> latitudes, ArrayList<Float> longitudes) {
        return positionPersistent.addRoute(latitudes,longitudes);
    }
}
