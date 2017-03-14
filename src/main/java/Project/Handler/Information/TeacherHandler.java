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

    public ArrayList<Teacher> getTeachersByStudentId(int personId) {
        ArrayList<Teacher> teachers = teacherPersistent.getTeachersByStudentId(personId, "" + Calendar.getInstance().get(Calendar.YEAR));
        for (Teacher it : teachers) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        return teachers;
    }

    public ArrayList<Teacher> getCurrentTeacherInBusByCarId(int carId, ArrayList<Timestamp> startAndEndPeriod) {
        return teacherPersistent.getCurrentTeachersInBusByCarId(carId, startAndEndPeriod);
    }

    public ArrayList<Teacher> getCurrentAllTeacherByCarId(int carId) {
        return teacherPersistent.getCurrentAllTeacherByCarId(carId);
    }

    public boolean addTeacherAndStudentRelationships(int personTId, ArrayList<Integer> personSIds, String classRoomName){
        boolean result = true;
        for(int it: personSIds) {
            if(!teacherPersistent.addTeacherAndStudentRelationship(personTId,it, "" + Calendar.getInstance().get(Calendar.YEAR), classRoomName)){
                result = false;
            }
        }
        return result;
    }
}
