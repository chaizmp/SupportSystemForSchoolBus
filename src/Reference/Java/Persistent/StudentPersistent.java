package Project.PersistentLayer;

import Project.Mapper.StudentMapper;
import Project.Model.ClassRoom;
import Project.Model.Person.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 30/8/2559.
 */
@Service
public class StudentPersistent extends JdbcTemplate{

    @Autowired
    public StudentPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public void addStudent(String id, String name, String surName, String telNum, String address,String pic) {
        update("INSERT INTO student(`id`, `name`,'surName','telNum','address','pic') VALUES (?,?,?,?,?,?)",new Object[]{id,name,surName,telNum,address,pic}); // not yet completed
    }

    public ArrayList<Student> getAllStudentByParentId(String id)
    {
        ArrayList<Student> stdArr = new ArrayList<>();
        List<Student> stdList = query("SELECT * FROM student WHERE studentId in (SELECT studentId FROM family WHERE parentId = '"+id +"' ) ORDER BY name " +
                "ASC", new StudentMapper());
        for(Student it : stdList)
        {
            if(it != null)
            stdArr.add(it);
        }
        return stdArr;
    }

    public ArrayList<Student> getAllStudentByTeacherId(String id)
    {
        ArrayList<Student> stdArr = new ArrayList<>();
        List<Student> stdList = query("SELECT * FROM student WHERE teacherId = "+"'"+id +"'"+" ORDER BY name " +
                "ASC", new StudentMapper());
        for(Student it : stdList)
        {
            if(it != null)
                stdArr.add(it);
        }
        return stdArr;
    }


}
