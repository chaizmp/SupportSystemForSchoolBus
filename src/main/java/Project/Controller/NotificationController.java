package Project.Controller;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Notification.NotificationHandler;
import Project.Model.Notification.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by User on 27/2/2560.
 */
@RestController
public class NotificationController {
    @Autowired
    NotificationHandler notificationHandler;
    @RequestMapping( value ="studentArrival", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<String> studentArrival(
            @RequestParam(value="personId") String personId,
            @RequestParam(value="carNumber") String carNumber
    ){

        return notificationHandler.arrival(personId, carNumber,true);
    }

    @RequestMapping( value ="schoolArrival", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<String> schoolArrival(
            @RequestParam(value="personId") String personId,
            @RequestParam(value="carNumber") String carNumber
    ){

        return notificationHandler.arrival(personId, carNumber,false);
    }
}
