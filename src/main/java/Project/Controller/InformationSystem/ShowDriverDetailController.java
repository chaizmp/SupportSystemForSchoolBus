package Project.Controller.InformationSystem;

import Project.Model.Driver;
import Project.Model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * Created by User on 29/8/2559.
 */
@Controller
public class ShowDriverDetailController {
    @RequestMapping(value = "showDriverDetail", method = RequestMethod.POST)
    public @ResponseBody
    Driver showDriverDetail( // method body not yet finished
                                      @RequestParam(value = "carNumber") String carNumber //teacher or parent
    )
    {
        return null; // returns all students, their teacher (if id is from parent) / their parent (if id is from  teacher)
        // and Bus where they are  ( with button to show the drivers)

    }
}
