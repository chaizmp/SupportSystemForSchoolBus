package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class ClassRoom {
    private ClassRoomName name;
    private ArrayList<Student> students;
    private Teacher teacher;

    public ClassRoom(ClassRoomName name, ArrayList<Student> students, Teacher teacher) {
        this.name = name;
        this.students = students;
        this.teacher = teacher;
    }

    public ClassRoomName getName() {
        return name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setName(ClassRoomName name) {
        this.name = name;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
