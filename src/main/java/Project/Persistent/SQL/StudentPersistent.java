package Project.Persistent.SQL;

import Project.Mapper.AddressMapper;
import Project.Mapper.StudentMapper;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Student;
import Project.Model.Position.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
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

    public boolean setTypeOfService(TypeOfService typeOfService, String personId)
    {
        boolean result = false;
        try {
            result = update("UPDATE `STUDENT` SET `typeOfService`= ? WHERE `personId` = ?",typeOfService.name(),personId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Role getRoleByPersonId(String personId) {
        Role result = null;
        try{
            result = Role.valueOf(queryForObject("SELECT role FROM PERSON WHERE personId = ?",String.class,personId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Student> getAllStudentByParentId(String personId) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                        "SELECT personSID from family WHERE personPID = "+personId +") ORDER BY person.name"+
                " ASC", new StudentMapper());
        return new ArrayList<>(studentList);

    }
    public ArrayList<Student> getAllStudentByTeacherId(String personId, String year) {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId and person.personId in ( " +
                "SELECT personSID from teachHistory WHERE personTID = "+personId +" AND year = "+year+") ORDER BY person.name"+
                " ASC", new StudentMapper());
        return new ArrayList<>(studentList);

    }

    public ArrayList<Student> getAllStudent() {
        List<Student> studentList = query("SELECT * FROM person,student WHERE person.personId = student.personId ORDER BY person.name ASC"
                , new StudentMapper());
        return new ArrayList<>(studentList);

    }
}
