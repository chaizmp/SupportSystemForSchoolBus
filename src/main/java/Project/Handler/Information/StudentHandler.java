package Project.Handler.Information;

import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import Project.Model.Person.Teacher;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.PositionPersistent;
import Project.Persistent.SQL.StudentPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
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
    @Autowired
    PositionPersistent positionPersistent;

    public boolean setTypeOfService(TypeOfService typeOfService,String personId)
    {
        return studentPersistent.setTypeOfService(typeOfService,personId);
    }
    public ArrayList<Student> getAllStudentByPersonId(String personId)
    {
        ArrayList<Student> students = null;
        Role role = studentPersistent.getRoleByPersonId(personId);
        if(role != null) {
            switch (role) {
                case TEACHER:
                    students = studentPersistent.getAllStudentByTeacherId(personId, "" + Calendar.getInstance().get(Calendar.YEAR));
                    break;
                case PARENT:
                    students = studentPersistent.getAllStudentByParentId(personId);
                    break;
                case SCHOOLOFFICER:
                    students = studentPersistent.getAllStudent();
                    break;
                default:
                    break;
            }
            for (Student it : students) {
                IsInBus inBus = positionPersistent.isInBus(it.getId());
                it.setInBus(inBus);
                it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
            }
        }
        return students;
    }

    public Student getStudentByPersonId(String personId){
        Student student = studentPersistent.getStudentByPersonId(personId);
        IsInBus inBus = positionPersistent.isInBus(personId);
        student.setInBus(inBus);
        return student;
    }

    public ArrayList<Student> getCurrentAllStudentByCarNumber(String carNumber) {
        return studentPersistent.getCurrentAllStudentByCarNumber(carNumber);
    }

    public int getNumberOfStudentInCurrentTrip(TypeOfService typeOfService){
        return studentPersistent.getNumberOfStudentInCurrentTrip(typeOfService);
    }

    public int getNumberOfStudentGetOutInCurrentTripExceptPersonId(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight){
        return studentPersistent.getNumberOfStudentGetOutInCurrentTripExceptPersonId(personId, carNumber, now, lunch, midNight);
    }

}
