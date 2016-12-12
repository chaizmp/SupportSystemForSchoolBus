package Project.Controller;

import Project.Model.Bus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by User on 29/8/2559.
 */
@Controller
public class PositioningController {
    @RequestMapping(value = "getBusDetail", method = RequestMethod.POST)
    public @ResponseBody
    Bus getBusDetail( //body not yet finished
            @RequestParam(value = "studentId") String studentId
    )
    {
        return null;
    }
    @RequestMapping(value = "getBusPosition", method = RequestMethod.POST)
    public @ResponseBody
    String getBusPositionl( //body not yet finished return as JSON String format
            @RequestParam(value = "carNumber") String carNumber
    )
    {
        return "hello";
    }

    @RequestMapping(value = "timeEstimation", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    String timeEstimation(
            @RequestParam(value = "carNumber") String carNumber
    )
    {
        return "hello";
    }

    @RequestMapping(value = "getRoute", method = RequestMethod.POST)
    public @ResponseBody // return type and body not yet finished
    String getRoute( //get all possible route to the student's home or the school
            @RequestParam(value = "studentId") String studentId
    )
    {
        return "hello";
    }


}
