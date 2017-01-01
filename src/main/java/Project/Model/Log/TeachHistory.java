package Project.Model.Log;

import Project.Model.Enumerator.ClassRoomName;
import Project.Model.Person.Student;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class TeachHistory {
    private ArrayList<ArrayList<Student>> studentGroups;
    private ArrayList<ClassRoomName> classRoomNames;
    private ArrayList<Date> years;

    public TeachHistory(ArrayList<ArrayList<Student>> studentGroups, ArrayList<ClassRoomName> classRoomNames, ArrayList<Date> years) {
        this.studentGroups = studentGroups;
        this.classRoomNames = classRoomNames;
        this.years = years;
    }

    public ArrayList<ArrayList<Student>> getStudentGroups() {
        return studentGroups;
    }

    public void setStudentGroups(ArrayList<ArrayList<Student>> studentGroups) {
        this.studentGroups = studentGroups;
    }

    public ArrayList<ClassRoomName> getClassRoomNames() {
        return classRoomNames;
    }

    public void setClassRoomNames(ArrayList<ClassRoomName> classRoomNames) {
        this.classRoomNames = classRoomNames;
    }

    public ArrayList<Date> getYears() {
        return years;
    }

    public void setYears(ArrayList<Date> years) {
        this.years = years;
    }
}
