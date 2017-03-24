package Project.Persistent.SQL;

import Project.Mapper.StudentMapper;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sun.security.ec.ECDHKeyAgreement;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class StudentPersistent extends JdbcTemplate {

    @Autowired
    public StudentPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public boolean setTypeOfService(TypeOfService typeOfService, int personId) {
        boolean result = false;
        try {
            result = update("UPDATE `STUDENT` SET `typeOfService`= ? WHERE `personId` = ?", typeOfService.name(), personId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Role getRoleByPersonId(int personId) {
        Role result;
        try {

            result = Role.valueOf(queryForObject("SELECT role FROM PERSON WHERE personId = ?", String.class, personId));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public ArrayList<Student> getAllStudentByParentId(int personId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from family WHERE personPID = ? ) ORDER BY person.name" +
                " ASC", new StudentMapper(), personId);
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudentByDriverId(int personId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from family WHERE personPID = ? ) ORDER BY person.name" +
                " ASC", new StudentMapper(), personId);
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudentByTeacherId(int personId, String year) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from teachHistory WHERE personTID = ? AND year = ?) ORDER BY person.name" +
                " ASC", new StudentMapper(), personId, year);
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudent() {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId ORDER BY person.name ASC"
                , new StudentMapper());
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudentByTypeOfService(TypeOfService typeOfService){
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId AND (student.typeOfService = ? OR student.typeOfService = ?) ORDER BY person.name ASC"
                , new StudentMapper(), typeOfService.name(), TypeOfService.BOTH.name());
        return new ArrayList<>(studentList);
    }


    public Student getStudentByPersonId(int personId) {
        Student result;
        try {

            result = queryForObject("SELECT * FROM person,student WHERE person.personId = student.personId AND student.personId = ?", new StudentMapper(), personId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public ArrayList<Student> getCurrentAllStudentByCarId(int carId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId AND student.personId IN " +
                "( SELECT personId from personInBus P1 WHERE carId = ? AND atTime >= " +
                "( SELECT MAX(atTime) FROM personInBus WHERE carId = ? AND status = 'START' " +
                ") AND isInBus = 'YES' AND atTime =  " +
                "(SELECT MAX(atTime) FROM personInBus P2 WHERE P1.personId = P2.personId AND carId = ?)" +
                ")", new StudentMapper(), carId, carId, carId);
        return new ArrayList<>(studentList);
    }

    public int getNumberOfStudentInCurrentTrip(int carId, TypeOfService typeOfService) {
        return queryForObject("SELECT COUNT(*) FROM student WHERE typeOfService = ? OR typeOfService = ? AND carId = ?", Integer.class, typeOfService.name(), TypeOfService.BOTH.name(), carId);
    }

    public int getNumberOfStudentGetOutInCurrentTripExceptPersonId(int personId, int carId, Timestamp now, Timestamp lunch, Timestamp midNight) {
        if (now.getTime() <= lunch.getTime()) {
            return queryForObject("SELECT COUNT(*) FROM personInBus WHERE personId != ? " +
                    "AND atTime <= ? " +
                    "AND atTime >= ? " +
                    "AND carId = ? " +
                    "AND isInBus = ? " +
                    "AND personId NOT IN ( SELECT personId FROM person WHERE role != ? ) ", Integer.class, personId, lunch, midNight, carId, IsInBus.NO.name(), Role.STUDENT.name());
        } else {
            return queryForObject("SELECT COUNT(*) FROM personInBus WHERE personId != ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND carId = ? " +
                    "AND isInBus = ? " +
                    "AND personId NOT IN ( SELECT personId FROM person WHERE role != ? ) ", Integer.class, personId, lunch, midNight, carId, IsInBus.NO.name(), Role.STUDENT.name());
        }
    }

    public boolean addStudentsTrip(int personIds, int carId){
        String carIdSQL = carId==-1? null: ""+carId;
        try {
            return update("UPDATE student SET carId = ? WHERE personId = ?", carIdSQL, personIds) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addStudent(String studentId, TypeOfService typeOfService, int personId){
        try{
            return update("INSERT INTO STUDENT(studentId, typeOfService, personId) VALUES(?, ?, ?)", studentId, typeOfService.name(), personId) == 1;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public ArrayList<Student> getAllStudentInCurrentTrip(int carId, TypeOfService typeOfService){
        try{
            List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId AND student.carId = ? AND (student.typeOfService = ? OR student.typeOfService = ?) ORDER BY person.name ASC"
                    , new StudentMapper(), carId, typeOfService.name(), TypeOfService.BOTH.name());
            return new ArrayList<>(studentList);
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean cancelStudentTrip(int personId){
        try{
            return update("UPDATE student SET carId = ? WHERE personId = ?", null, personId) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
   /* public ArrayList<Timestamp> getCurrentStartAndEndPeriodByStudentId(String personId){
        ArrayList<Timestamp> result = new ArrayList<>();
        Timestamp atTime,start,end;
        try {
            atTime = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE personId = ? LIMIT 1", Timestamp.class,personId);
        } catch (Exception e){
            atTime = null;
        }
        try {
            start = queryForObject("SELECT MAX(atTime) FROM PersonInBus " +
                    "WHERE atTime <= ? "+
                    "AND STATUS = 'START'", Timestamp.class,atTime);
        }catch (Exception e){
            start = null;
        }
        try {
            end = queryForObject("SELECT MIN(atTime) FROM PersonInBus " +
                    "WHERE atTime >= ? " +
                    "AND STATUS = 'FINISH'", Timestamp.class, atTime);
        }catch (Exception e){
            end = null;
        }
        result.add(start);
        result.add(end);
        return result;
    } */
}
