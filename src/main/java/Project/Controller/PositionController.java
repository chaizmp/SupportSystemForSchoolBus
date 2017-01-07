package Project.Controller;

import Project.Handler.Information.BusHandler;
import Project.Handler.Information.StudentHandler;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.Status;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 1/1/2560.
 */
@RestController
public class PositionController {

    @Autowired
    BusHandler busHandler;
    @Autowired
    StudentHandler studentHandler;
    @Autowired
    PositionHandler positionHandler;

    @RequestMapping(value = "getBusCurrentPosition", method = RequestMethod.POST)
    public @ResponseBody
    Bus getBusCurrentPosition(
                            @RequestParam(value = "carNumber") String carNumber
    )
    {
        return busHandler.getCurrentBusPosition(carNumber);
    }

    @RequestMapping(value = "timeEstimation", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    String timeEstimation(
            @RequestParam(value = "carNumber") String carNumber
    )
    {
        return "hello";
    }

    @RequestMapping(value = "getCurrentRoute", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    ArrayList<Position> getCurrentRoute( //get all possible route to the student's home or the school
                                  @RequestParam(value = "personId") String personId
    )
    {
        Timestamp atTime = positionHandler.getLatestAtTimeByStudentId(personId);
        String carNumber = busHandler.getBusCarNumberByStudentIdAndAtTime(personId,atTime).getCarNumber();
        return positionHandler.getActualRouteInTripByAtTime(carNumber,atTime);
    }

    @RequestMapping(value = "addBusPosition", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    boolean addBusPosition( //get all possible route to the student's home or the school
                            @RequestParam(value = "carNumber") String personId,
                            @RequestParam(value = "latitude") float latitude,
                            @RequestParam(value = "longitude") float longitude,
                            @RequestParam(value = "status") Status status
                            )
    {
        return positionHandler.addBusPosition(personId,latitude,longitude,status);
    }


    @RequestMapping(value = "addRoute", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    boolean addBusPosition( //get all possible route to the student's home or the school
                            @RequestParam(value = "latitudes") List<Float> latitudes,
                            @RequestParam(value = "longitudes") List<Float> longitudes
    )
    {
        return positionHandler.addRoute(new ArrayList<>(latitudes),new ArrayList<>(longitudes));
    }
}
