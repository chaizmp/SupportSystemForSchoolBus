package Project.Handler.Information;

import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.Status;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import Project.Persistent.SQL.BusPersistent;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.PositionPersistent;
import Project.Persistent.SQL.StudentPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by User on 1/1/2560.
 */
@Service
public class StudentHandler {

    @Autowired
    StudentPersistent studentPersistent;
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    PositionPersistent positionPersistent;
    @Autowired
    PositionHandler positionHandler;
    @Autowired
    BusPersistent busPersistent;

    public boolean setTypeOfService(TypeOfService typeOfService, String personId) {
        return studentPersistent.setTypeOfService(typeOfService, personId);
    }

    public ArrayList<Student> getAllStudentByPersonId(String personId) {
        ArrayList<Student> students = null;
        Role role = studentPersistent.getRoleByPersonId(personId);
        if (role != null) {
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

    public Student getStudentByPersonId(String personId) {
        Student student = studentPersistent.getStudentByPersonId(personId);
        IsInBus inBus = positionPersistent.isInBus(personId);
        student.setInBus(inBus);
        return student;
    }

    public ArrayList<Student> getCurrentAllStudentByCarNumber(String carNumber) {
        return studentPersistent.getCurrentAllStudentByCarNumber(carNumber);
    }

    public int getNumberOfStudentInCurrentTrip(String carNumber, TypeOfService typeOfService) {
        return studentPersistent.getNumberOfStudentInCurrentTrip(carNumber, typeOfService);
    }

    public int getNumberOfStudentGetOutInCurrentTripExceptPersonId(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight) {
        return studentPersistent.getNumberOfStudentGetOutInCurrentTripExceptPersonId(personId, carNumber, now, lunch, midNight);
    }

    public boolean isEveryStudentGetsOnTheBus(String carNumber){
        TypeOfService typeOfService;
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 12); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Timestamp lunch = new Timestamp(c.getTimeInMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp midNight = new Timestamp(c.getTimeInMillis());
        if(now.getTime() >= lunch.getTime()){
            typeOfService = TypeOfService.BACK;
        }
        else{
            typeOfService = TypeOfService.GO;
        }
        int numStudentsInTrip = getNumberOfStudentInCurrentTrip(carNumber, typeOfService);
        return getStudentsUsedToBeOnBusInCurrentTrip(carNumber, now, lunch, midNight) == numStudentsInTrip;
    }

    public int getStudentsUsedToBeOnBusInCurrentTrip(String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight){
        return positionPersistent.getStudentsUsedToBeOnBusInCurrentTrip(carNumber, now, lunch, midNight);
    }

    public boolean addStudentsTrip(ArrayList<String> personIds, String carNumber){
        boolean result = true;
        for(String it: personIds) {
             if(!studentPersistent.addStudentsTrip(it, carNumber)){
                 result= false;
             }
        }
        return result;
    }
}
