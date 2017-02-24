package Project.Handler.Information;

import Project.Model.Person.Teacher;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.TeacherPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class TeacherHandler {

    @Autowired
    TeacherPersistent teacherPersistent;
    @Autowired
    PersonPersistent personPersistent;

    public ArrayList<Teacher> getTeachersByStudentId(String personId) {
        ArrayList<Teacher> teachers = teacherPersistent.getTeachersByStudentId(personId, "" + Calendar.getInstance().get(Calendar.YEAR));
        for (Teacher it : teachers) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        return teachers;
    }

    public ArrayList<Teacher> getCurrentTeacherInBusByCarNumber(String carNumber, ArrayList<Timestamp> startAndEndPeriod) {
        return teacherPersistent.getCurrentTeachersInBusByCarNumber(carNumber, startAndEndPeriod);
    }

    public ArrayList<Teacher> getCurrentAllTeacherByCarNumber(String carNumber) {
        return teacherPersistent.getCurrentAllTeacherByCarNumber(carNumber);
    }
}
