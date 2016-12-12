package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Teacher extends Human{

    private ClassRoomName classRoomName;
    public Teacher(String id, String name, String surName, String telNum, String address, String classRoomName) {
        super(id, name, surName, telNum, address);
        this.classRoomName = ClassRoomName.valueOf(classRoomName);
    }

    public ClassRoomName getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(ClassRoomName classRoomName) {
        this.classRoomName = classRoomName;
    }
}
