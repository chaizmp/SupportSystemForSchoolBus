package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Teacher extends Human{
    private ClassRoom classRoom;
    private ArrayList<Student> students;

    public Teacher(String id, String name, String surName, String telNum, String address, ArrayList<Student> students, ClassRoom classRoom) {
        super(id, name, surName, telNum, address);
        this.students = students;
        this.classRoom = classRoom;
    }

    public Teacher(String id, String name, String surName, String telNum, String address) {
        super(id, name, surName, telNum, address);
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
