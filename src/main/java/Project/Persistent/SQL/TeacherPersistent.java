package Project.Persistent.SQL;

import Project.Mapper.PersonMapper;
import Project.Mapper.TeacherMapper;
import Project.Model.Person.Person;
import Project.Model.Person.Teacher;
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
public class TeacherPersistent extends JdbcTemplate {

    @Autowired
    public TeacherPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Teacher> getTeachersByStudentId(int personId, String year) {

        try {
            List<Teacher> teacherList = query("SELECT * FROM Person,Teacher WHERE Person.personId = Teacher.personId and Person.personId in (" +
                    "SELECT personTId from TeachHistory WHERE year = ? AND personSID = ? )", new TeacherMapper(), year, personId);
            return new ArrayList<>(teacherList);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Teacher> getCurrentTeachersInBusByCarId(int carId, ArrayList<Timestamp> startAndEndPeriod) {
        List<Teacher> teacherList;
        if (startAndEndPeriod.get(1) != null) {
            teacherList = query("SELECT DISTINCT Person.Role, Person.PersonId, Person.Token, Person.Tel, Person.username, Person.name, Person.surname, Person.facebookId FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'TEACHER' " +
                    "AND atTime >=  ? AND atTime <= ? " +
                    "AND carId = ? ", new TeacherMapper(), startAndEndPeriod.get(0), startAndEndPeriod.get(1), carId);
        } else {
            teacherList = query("SELECT DISTINCT Person.Role, Person.PersonId, Person.Token, Person.Tel, Person.username, Person.name, Person.surname, Person.facebookId FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'TEACHER' " +
                    "AND atTime >= ? " +
                    "AND carId = ?", new TeacherMapper(), startAndEndPeriod.get(0), carId);
        }
        return new ArrayList<>(teacherList);
    }

    public ArrayList<Teacher> getCurrentAllTeacherByCarId(int carId) {
        List<Teacher> teacherList = query("SELECT * FROM person,teacher WHERE person.personId = teacher.personId AND teacher.personId IN " +
                "( SELECT personId from personInBus P1 WHERE carId = ? AND atTime >= " +
                "( SELECT MAX(atTime) FROM personInBus WHERE carId = ? AND status = 'START' " +
                ") AND isInBus = 'YES' AND atTime =  " +
                "(SELECT MAX(atTime) FROM personInBus P2 WHERE P1.personId = P2.personId AND carId = ?)" +
                ")", new TeacherMapper(), carId, carId, carId);
        return new ArrayList<>(teacherList);
    }

    public boolean addTeacher(int personId){
        try{
            return update("INSERT INTO teacher(personId) VALUES(?)", personId) == 1;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public boolean addTeacherAndStudentRelationship(int personTId, int personSId, String year, String classRoomName){
        try{
            return update("INSERT INTO teachHistory(personTId, personSid, year, classRoomName) VALUES(?, ?, ?, ?)", personTId, personSId, year, classRoomName) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Person> getAllTeachers(){
        try{
            return new ArrayList<>(query("SELECT * FROM Person WHERE Role = 'TEACHER'", new PersonMapper()));
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean clearTeachHistory(int personId){
        try{
            return update("DELETE FROM teachHistory WHERE personTId = ?", personId) >=0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean clearTeacher(int personId){
        try{
            return update("DELETE FROM teacher WHERE personId = ?", personId)>=0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
