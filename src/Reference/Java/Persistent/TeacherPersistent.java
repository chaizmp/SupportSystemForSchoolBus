package Project.PersistentLayer;

import Project.Mapper.TeacherMapper;
import Project.Model.Enumerator.ClassRoomName;
import Project.Model.Person.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by User on 30/8/2559.
 */
@Service
public class TeacherPersistent extends JdbcTemplate {
    @Autowired
    public TeacherPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public void addTeacher(String id, String name, String surName, String telNum, String address, ClassRoomName classRoomName) {
        update("INSERT INTO student(`id`, `name`,'surName','telNum','address','classRoomName') VALUES (?,?,?,?,?,?)", new Object[]{id, name, surName, telNum, address, classRoomName}); // not yet completed
    }

    public boolean teacherIsExist(String id) {
        if (queryForObject("SELECT count(*) from Teacher WHERE teacherId = ?", Integer.class, id) == 1) {
            return true;
        }
        return false;
    }

    public Teacher getTeacherByCarNum(String carNum) {
        List<Teacher> teaList = query("SELECT * FROM teacher where teacherId = (SELECT teacherId FROM BusAndTeacher WHERE time = (SELECT MAX(time) FROM BusAndTeacher WHERE carNumber = '" + carNum + "'))"
                , new TeacherMapper());
        Teacher teacher = null;
        for (Teacher it : teaList) {
            if (it != null) {
                teacher = it;
            }
        }
        return teacher;
    }

    public Teacher getTeacherByStudentId(String id) {
        List<Teacher> teaList = query("SELECT * FROM TEACHER WHERE teacherId = (SELECT teacherId FROM STUDENT  WHERE studentId =  '" + id + "')", new TeacherMapper());
        Teacher teacher = null;
        for (Teacher it : teaList) {
            if (it != null) {
                teacher = it;
            }
        }
        return teacher;
    }
}
