package Project.Handler.Information;

import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.StudentPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by User on 1/1/2560.
 */
@Service
public class StudentHandler{

    @Autowired
    StudentPersistent studentPersistent;
    @Autowired
    PersonPersistent personPersistent;

    public boolean setTypeOfService(TypeOfService typeOfService,String personId)
    {
        return studentPersistent.setTypeOfService(typeOfService,personId);
    }
    public ArrayList<Student> getAllStudentByPersonId(String personId)
    {
        ArrayList<Student> students = null;
        switch (studentPersistent.getRoleByPersonId(personId)){
            case TEACHER:       students = studentPersistent.getAllStudentByTeacherId(personId, ""+Calendar.getInstance().get(Calendar.YEAR));
                                break;
            case PARENT:        students = studentPersistent.getAllStudentByParentId(personId);
                                break;
            case SCHOOLOFFICER: students = studentPersistent.getAllStudent();
            default:            break;
        }
        for(Student it: students){
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        return students;
    }
}
