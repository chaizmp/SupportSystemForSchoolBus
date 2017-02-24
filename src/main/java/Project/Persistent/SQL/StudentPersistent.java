package Project.Persistent.SQL;

import Project.Mapper.StudentMapper;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

    public boolean setTypeOfService(TypeOfService typeOfService, String personId) {
        boolean result = false;
        try {
            result = update("UPDATE `STUDENT` SET `typeOfService`= ? WHERE `personId` = ?", typeOfService.name(), personId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Role getRoleByPersonId(String personId) {
        Role result;
        try {

            result = Role.valueOf(queryForObject("SELECT role FROM PERSON WHERE personId = ?", String.class, personId));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public ArrayList<Student> getAllStudentByParentId(String personId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from family WHERE personPID = ? ) ORDER BY person.name" +
                " ASC", new StudentMapper(), personId);
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudentByDriverId(String personId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from family WHERE personPID = ? ) ORDER BY person.name" +
                " ASC", new StudentMapper(), personId);
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudentByTeacherId(String personId, String year) {
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

    public Student getStudentByPersonId(String personId) {
        Student result;
        try {

            result = queryForObject("SELECT * FROM person,student WHERE person.personId = student.personId AND student.personId = ?", new StudentMapper(), personId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public ArrayList<Student> getCurrentAllStudentByCarNumber(String carNumber) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId AND student.personId IN " +
                "( SELECT personId from personInBus P1 WHERE carNumber = ? AND atTime >= " +
                "( SELECT MAX(atTime) FROM personInBus WHERE carNumber = ? AND status = 'START' " +
                ") AND isInBus = 'YES' AND atTime =  " +
                "(SELECT MAX(atTime) FROM personInBus P2 WHERE P1.personId = P2.personId AND carNumber = ?)" +
                ")", new StudentMapper(), carNumber, carNumber, carNumber);
        return new ArrayList<>(studentList);
    }

    public int getNumberOfStudentInCurrentTrip(TypeOfService typeOfService){
        return queryForObject("SELECT COUNT(*) FROM student WHERE typeOfService = ? OR typeOfService = ?", Integer.class, typeOfService.name(), TypeOfService.BOTH.name());
    }

    public int getNumberOfStudentGetOutInCurrentTripExceptPersonId(String personId, String carNumber, Timestamp now, Timestamp lunch, Timestamp midNight){
        if(now.getTime() <= lunch.getTime()) {
            return queryForObject("SELECT COUNT(*) FROM personInBus WHERE personId != ? " +
                    "AND atTime <= ? " +
                    "AND atTime >= ? " +
                    "AND carNumber = ? " +
                    "AND isInBus = ? " +
                    "AND personId NOT IN ( SELECT personId FROM person WHERE role != ? ) ", Integer.class, personId, lunch, midNight, carNumber, IsInBus.NO.name(), Role.STUDENT.name());
        }
        else{
            return queryForObject("SELECT COUNT(*) FROM personInBus WHERE personId != ? " +
                    "AND atTime >= ? " +
                    "AND atTime >= ? " +
                    "AND carNumber = ? " +
                    "AND isInBus = ? " +
                    "AND personId NOT IN ( SELECT personId FROM person WHERE role != ? ) ", Integer.class, personId, lunch, midNight, carNumber, IsInBus.NO.name(), Role.STUDENT.name());
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
