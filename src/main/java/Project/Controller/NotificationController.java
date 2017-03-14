package Project.Controller;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.PersonHandler;
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
    @Autowired
    PersonHandler personHandler;

    @RequestMapping( value ="studentArrival", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<String> studentArrival(
            @RequestParam(value="personId") int personId,
            @RequestParam(value="carNumber") String carNumber
    ){

        return notificationHandler.arrival(personId, carNumber, true);
    }

    @RequestMapping( value ="schoolArrival", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<String> schoolArrival(
            @RequestParam(value="personId") int personId,
            @RequestParam(value="carNumber") String carNumber
    ){

        return notificationHandler.arrival(personId, carNumber, false);
    }

    @RequestMapping( value ="notify", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<String> notifyAll(
            @RequestParam(value="personIds") ArrayList<Integer> personIds,
            @RequestParam(value="carNumber") String carNumber,
            @RequestParam(value="message") String message
    ){

        return notificationHandler.notifyBus(personIds, carNumber, message);
    }

    @RequestMapping( value ="setAlarm", method = RequestMethod.POST)
    public @ResponseBody
    boolean setAlarm(
            @RequestParam(value="personId") int personId,
            @RequestParam(value="duration") int duration
    ){

        return personHandler.setAlarm(personId, duration);
    }

}
