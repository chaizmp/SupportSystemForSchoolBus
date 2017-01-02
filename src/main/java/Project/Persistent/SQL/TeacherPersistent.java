package Project.Persistent.SQL;

import Project.Mapper.AddressMapper;
import Project.Mapper.TeacherMapper;
import Project.Model.Person.Teacher;
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
public class TeacherPersistent extends JdbcTemplate {

    @Autowired
    public TeacherPersistent(DataSource mainDataSource)
    {
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Teacher> getTeachersByStudentId(String personId, String year)
    {

        List<Teacher> teacherList =  query("SELECT * FROM Person,Teacher WHERE Person.personId = Teacher.personId and Person.personId in (" +
                "SELECT personTId from TeachHistory WHERE year = "+year+" AND personSID = "+personId+" )",new TeacherMapper());
        return new ArrayList<>(teacherList);
    }
}