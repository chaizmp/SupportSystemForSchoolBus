package Project.Controller;

import Project.Handler.Information.BusHandler;
import Project.Handler.Information.StudentHandler;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.Status;
import Project.Model.Position.Bus;
import Project.Model.Position.Position;
import Project.Model.Position.Route;
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
    public @ResponseBody
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
                            @RequestParam(value = "carNumber") String carNumber,
                            @RequestParam(value = "latitude") double latitude,
                            @RequestParam(value = "longitude") double longitude,
                            @RequestParam(value = "status") Status status
                            )
    {
        boolean result =  positionHandler.addBusPosition(carNumber,latitude,longitude,status);
        positionHandler.calculateVelocity(carNumber);
        return result;
    }

    @RequestMapping(value = "getOnOrOffBus", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    boolean addBusPosition( //get all possible route to the student's home or the school
                            @RequestParam(value = "carNumber") String carNumber,
                            @RequestParam(value = "personId") String personId,
                            @RequestParam(value = "latitude") double latitude,
                            @RequestParam(value = "longitude") double longitude
    )
    {
        return positionHandler.getOnOrOffBus(carNumber, personId, latitude, longitude);
    }

    @RequestMapping(value = "addRoute", method = RequestMethod.POST)
    public @ResponseBody
    Integer addRoute(
                            @RequestParam(value = "latitudes") ArrayList<Double> latitudes,
                            @RequestParam(value = "longitudes") ArrayList<Double> longitudes
    )
    {
        return positionHandler.addRoute(latitudes,longitudes);
    }

    @RequestMapping(value = "getAllBusRoute", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Route> getAllBusRoute()
    {
        return positionHandler.getAllBusRoute();
    }

    @RequestMapping(value = "deleteRoute", method = RequestMethod.POST)
    public @ResponseBody
    boolean deleteRoute(
            @RequestParam(value = "routeNumber") int routeNumber
    )
    {
        return positionHandler.deleteRoute(routeNumber);
    }

    @RequestMapping(value = "getAllCurrentBusPosition", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Bus> getAllCurrentBusPosition(

    ){
        return positionHandler.getAllCurrentBusPosition();
    }

    @RequestMapping(value= "setBusRoute", method = RequestMethod.POST)
    public @ResponseBody
    boolean setBusRoute(
            @RequestParam(value = "carNumber") String carNumber,
            @RequestParam(value = "routeNumber") int routeNumber
    ){
        return positionHandler.setBusRoute(carNumber,routeNumber);
    }

    @RequestMapping(value= "calculateSpeed", method = RequestMethod.POST)
    public @ResponseBody
    boolean setBusRoute(
            @RequestParam(value = "carNumber") String carNumber
    ){
        //return positionHandler.calculateSpeed(carNumber);
        return true;
    }
}
