package Project.Controller.InformationComponent;
import Project.Model.Student;
import Project.PersistentLayer.*;
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
public class ShowStudentInformationController {

    @Autowired
    ParentPersistent parPer;
    @Autowired
    TeacherPersistent teaPer;
    @Autowired
    StudentPersistent stuPer;
    @Autowired
    BusPersistent busPer;
    @Autowired
    DriverPersistent driPer;

    @RequestMapping(value = "getAllStudentInformation", method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Student> getAllStudent( // method body not yet finished
        @RequestParam(value = "id") String id //teacher or parent
    )
    {
        ArrayList<Student> stuList = new ArrayList<>();
        if(parPer.parentIsExist(id))
        {
            stuList = stuPer.getAllStudentByParentId(id);
            for(Student it:stuList)
            {
                it.setTeacherOrParent(teaPer.getTeacherByStudentId(it.getId()));
                it.setBus(busPer.getBusByStudentId(it.getId()));
            }
        }
        else if(teaPer.teacherIsExist(id))
        {
            stuList = stuPer.getAllStudentByTeacherId(id);
            for(Student it:stuList)
            {
                it.setTeacherOrParent(parPer.getParentByStudentId(it.getId()));
                it.setBus(busPer.getBusByStudentId(it.getId()));
            }
        }
        return stuList; // returns all students, their teacher (if id is from parent) / their parent (if id is from  teacher)
        // and Bus where they are  ( with button to show the drivers)

    }
}
