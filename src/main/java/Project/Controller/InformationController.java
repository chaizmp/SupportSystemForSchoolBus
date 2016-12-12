package Project.Controller;

import Project.Model.Student;
import Project.Model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InformationController {

    @RequestMapping(value = "getTeacher", method = RequestMethod.POST)
    public @ResponseBody
    Teacher getTeacher( // method body not yet finished
            @RequestParam(value = "studentId") String studentId
    )
    {
        return new Teacher("1","1","1","1","1","1");
    }
    @RequestMapping(value = "getAllStudent", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Student> getAllStudent( // method body not yet finished
            @RequestParam(value = "id") String id
    )
    {
        return new ArrayList<Student>();
    }
}
