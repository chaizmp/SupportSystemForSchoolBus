package Project.Model.Log;

import Project.Model.Enumerator.ClassRoomName;
import Project.Model.Person.Teacher;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class ClassHistory {
    private ArrayList<Teacher> teachers;
    private ArrayList<ClassRoomName> classRoomNames;

    public ClassHistory(ArrayList<Teacher> teachers, ArrayList<ClassRoomName> classRoomNames) {
        this.teachers = teachers;
        this.classRoomNames = classRoomNames;
    }
}
