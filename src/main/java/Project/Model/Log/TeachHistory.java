package Project.Model.Log;

import Project.Model.Enumerator.ClassRoomName;
import Project.Model.Person.Student;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class TeachHistory {
    private ArrayList<ArrayList<Student>> studentGroups;
    private ArrayList<ClassRoomName> classRoomNames;
    private ArrayList<Date> years;

    public TeachHistory(ArrayList<ArrayList<Student>> studentGroups, ArrayList<ClassRoomName> classRoomNames, ArrayList<Date> years) {
        this.studentGroups = studentGroups;
        this.classRoomNames = classRoomNames;
        this.years = years;
    }
}
