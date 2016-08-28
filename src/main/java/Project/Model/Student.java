package Project.Model;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
/**
 * Created by User on 28/8/2559.
 */
public class Student extends Human {
    private ClassRoom classRoom;
    private Teacher teacher;
    private ArrayList<Parent> parents;
    private BufferedImage pic;

    public Student(String id, String name, String surName, String telNum, String address, ClassRoom classRoom, Teacher teacher, ArrayList<Parent> parents) {
        super(id, name, surName, telNum, address);
        this.classRoom = classRoom;
        this.teacher = teacher;
        this.parents = parents;
    }

    public Student(String id, String name, String surName, String telNum, String address) {
        super(id, name, surName, telNum, address);
    }

    public ArrayList<Parent> getParents() {
        return parents;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setParents(ArrayList<Parent> parents) {
        this.parents = parents;
    }
}
