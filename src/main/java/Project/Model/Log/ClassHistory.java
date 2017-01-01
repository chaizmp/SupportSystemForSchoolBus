package Project.Model.Log;

import Project.Model.Enumerator.ClassRoomName;
import Project.Model.Person.Teacher;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class ClassHistory {
    private ArrayList<Teacher> teachers;
    private ArrayList<ClassRoomName> classRoomNames;

    public ClassHistory(ArrayList<Teacher> teachers, ArrayList<ClassRoomName> classRoomNames) {
        this.teachers = teachers;
        this.classRoomNames = classRoomNames;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<ClassRoomName> getClassRoomNames() {
        return classRoomNames;
    }

    public void setClassRoomNames(ArrayList<ClassRoomName> classRoomNames) {
        this.classRoomNames = classRoomNames;
    }
}
