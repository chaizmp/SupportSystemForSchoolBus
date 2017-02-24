package Project.Persistent.SQL;

import Project.Mapper.TeacherMapper;
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

    public ArrayList<Teacher> getTeachersByStudentId(String personId, String year) {

        try {
            List<Teacher> teacherList = query("SELECT * FROM Person,Teacher WHERE Person.personId = Teacher.personId and Person.personId in (" +
                    "SELECT personTId from TeachHistory WHERE year = ? AND personSID = ? )", new TeacherMapper(), year, personId);
            return new ArrayList<>(teacherList);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Teacher> getCurrentTeachersInBusByCarNumber(String carNumber, ArrayList<Timestamp> startAndEndPeriod) {
        List<Teacher> teacherList;
        if (startAndEndPeriod.get(1) != null) {
            teacherList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'TEACHER' " +
                    "AND atTime >=  ? AND atTime <= ? " +
                    "AND carNumber = ? ", new TeacherMapper(), startAndEndPeriod.get(0), startAndEndPeriod.get(1), carNumber);
        } else {
            teacherList = query("SELECT * FROM PersonInBus,Person WHERE Person.personId = PersonInBus.personId " +
                    "AND Person.role = 'TEACHER' " +
                    "AND atTime >= ? " +
                    "AND carNumber = ?", new TeacherMapper(), startAndEndPeriod.get(0), carNumber);
        }
        return new ArrayList<>(teacherList);
    }

    public ArrayList<Teacher> getCurrentAllTeacherByCarNumber(String carNumber) {
        List<Teacher> teacherList = query("SELECT * FROM person,teacher WHERE person.personId = teacher.personId AND teacher.personId IN " +
                "( SELECT personId from personInBus P1 WHERE carNumber = ? AND atTime >= " +
                "( SELECT MAX(atTime) FROM personInBus WHERE carNumber = ? AND status = 'START' " +
                ") AND isInBus = 'YES' AND atTime =  " +
                "(SELECT MAX(atTime) FROM personInBus P2 WHERE P1.personId = P2.personId AND carNumber = ?)" +
                ")", new TeacherMapper(), carNumber, carNumber, carNumber);
        return new ArrayList<>(teacherList);
    }

}
