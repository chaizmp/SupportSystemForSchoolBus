package Project.Controller;

import Project.Handler.Information.BusHandler;
import Project.Handler.Information.ParentHandler;
import Project.Handler.Information.StudentHandler;
import Project.Handler.Information.TeacherHandler;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Parent;
import Project.Model.Person.Student;
import Project.Model.Position.Bus;
import Project.Model.Person.Driver;
import Project.Model.Person.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    StudentHandler studentHandler;
    @Autowired
    TeacherHandler teacherHandler;
    @Autowired
    ParentHandler parentHandler;
    @Autowired
    BusHandler busHandler;

    @RequestMapping(value = "setTypeOfService", method = RequestMethod.POST)
    public @ResponseBody
    boolean setTypeOfService (@RequestParam(value = "typeOfService") TypeOfService typeOfService,
                              @RequestParam(value = "personId") String personId
    )
    {
        return studentHandler.setTypeOfService(typeOfService,personId);
    }

    @RequestMapping(value = "getTeachers", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Teacher> getTeachers( // method body not yet finished
            @RequestParam(value = "personId") String personId
    )
    {
        return teacherHandler.getTeachersByStudentId(personId);
    }

    @RequestMapping(value = "getParents", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Parent> getParents( // method body not yet finished
                                  @RequestParam(value = "personId") String personId
    )
    {
        return parentHandler.getParentsByStudentId(personId);
    }

    @RequestMapping(value = "showDriverDetail", method = RequestMethod.POST)
    public @ResponseBody
    Driver showDriverDetail( // method body not yet finished
                             @RequestParam(value = "carNumber") String carNumber //teacher or parent
    )
    {
        return null; // returns all students, their teacher (if id is from parent) / their parent (if id is from  teacher)
        // and Bus where they are  ( with button to show the drivers)

    }

    @RequestMapping(value = "getAllStudentInformation", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Student> getAllStudent( // method body not yet finished
                                      @RequestParam(value = "personId") String personId //teacher or parent
    )
    {
        return studentHandler.getAllStudentByPersonId(personId);
    }

    @RequestMapping(value = "getBusDetail", method = RequestMethod.POST)
    public @ResponseBody
    String getBusDetail( //body not yet finished
                      @RequestParam(value = "personId") String personId
    )
    {

    return null;

    }
}
