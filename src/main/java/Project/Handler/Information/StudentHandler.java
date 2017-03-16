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

    public boolean setTypeOfService(TypeOfService typeOfService, int personId) {
        return studentPersistent.setTypeOfService(typeOfService, personId);
    }

    public ArrayList<Student> getAllStudentByPersonId(int personId) {
        ArrayList<Student> students = null;
        if(personId != -1) {
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
        }
        else {
            TypeOfService typeOfService;
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 12); //anything 0 - 23
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            Timestamp lunch = new Timestamp(c.getTimeInMillis());
            c.set(Calendar.HOUR_OF_DAY, 0);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp midNight = new Timestamp(c.getTimeInMillis());
            Timestamp start;
            Timestamp end;
            if(now.getTime() >= lunch.getTime()){
                typeOfService = TypeOfService.BACK;
                start = lunch;
                end = null;
            }
            else{
                typeOfService = TypeOfService.GO;
                start = midNight;
                end = lunch;
            }
            students = studentPersistent.getAllStudentByTypeOfService(typeOfService);
            for (Student it : students) {
                IsInBus inBus = positionPersistent.isInBusPeriod(it.getId(), start, end); //PERIOD
                it.setInBus(inBus);
                it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
            }
        }
        return students;
    }

    public Student getStudentByPersonId(int personId) {
        Student student = studentPersistent.getStudentByPersonId(personId);
        IsInBus inBus = positionPersistent.isInBus(personId);
        student.setInBus(inBus);
        return student;
    }

    public ArrayList<Student> getCurrentAllStudentByCarId(int carId) {
        return studentPersistent.getCurrentAllStudentByCarId(carId);
    }

    public int getNumberOfStudentInCurrentTrip(int carId, TypeOfService typeOfService) {
        return studentPersistent.getNumberOfStudentInCurrentTrip(carId, typeOfService);
    }

    public int getNumberOfStudentGetOutInCurrentTripExceptPersonId(int personId, int carId, Timestamp now, Timestamp lunch, Timestamp midNight) {
        return studentPersistent.getNumberOfStudentGetOutInCurrentTripExceptPersonId(personId, carId, now, lunch, midNight);
    }

    public boolean isEveryStudentGetsOnTheBus(int carId){
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
        int numStudentsInTrip = getNumberOfStudentInCurrentTrip(carId, typeOfService);
        return getStudentsUsedToBeOnBusInCurrentTrip(carId, now, lunch, midNight) == numStudentsInTrip;
    }

    public int getStudentsUsedToBeOnBusInCurrentTrip(int carId, Timestamp now, Timestamp lunch, Timestamp midNight){
        return positionPersistent.getStudentsUsedToBeOnBusInCurrentTrip(carId, now, lunch, midNight);
    }

    public ArrayList<Integer> getAllStudentsUsedToBeOnBusInCurrentTrip(int carId, Timestamp now, Timestamp lunch, Timestamp midNight){
        return new ArrayList<>(positionPersistent.getAllStudentsUsedToBeOnBusInCurrentTrip(carId, now, lunch, midNight));
    }

    public boolean addStudentsTrip(ArrayList<Integer> personIds, int carId){
        boolean result = true;
        for(int it: personIds) {
             if(!studentPersistent.addStudentsTrip(it, carId)){
                 result= false;
             }
        }
        return result;
    }

    public ArrayList<Student> getAllStudentInCurrentTrip(int carId){
        TypeOfService typeOfService;
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 12); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Timestamp lunch = new Timestamp(c.getTimeInMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp midNight = new Timestamp(c.getTimeInMillis());
        Timestamp start;
        Timestamp end;
        if(now.getTime() >= lunch.getTime()){
            start = lunch;
            end = null;
            typeOfService = TypeOfService.BACK;
        }
        else{
            start = midNight;
            end = lunch;
            typeOfService = TypeOfService.GO;
        }

        ArrayList<Student> students = studentPersistent.getAllStudentInCurrentTrip(carId, typeOfService);
        for (Student it : students) {
            IsInBus inBus = positionPersistent.isInBusPeriod(it.getId(), start, end);
            it.setInBus(inBus);
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }

        return students;
    }
}
