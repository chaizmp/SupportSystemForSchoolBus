package Project.Model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
/**

 * Created by User on 28/8/2559.
 */
public class Student extends Human {
    private String pic;
    private TypeOfService typeOfService;
    private boolean isInBus;
    private Human teacherOrParent;
    private Bus bus;
    public Student(String id, String name, String surName, String telNum, String address) {
        super(id, name, surName, telNum, address);
    }

    public Student(String id, String name, String surName, String telNum, String address, String pic, String typeOfService, boolean isInBus) {
        super(id, name, surName, telNum, address);
        this.pic = pic;
        this.typeOfService = TypeOfService.valueOf(typeOfService);
        this.isInBus = isInBus;
    }

    public boolean isInBus() {
        return bus != null;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = TypeOfService.valueOf(typeOfService);
    }

    public Bus getBus() {
        return bus;
    }

    public Human getTeacherOrParent() {
        return teacherOrParent;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void setTeacherOrParent(Human teacherOrParent) {
        this.teacherOrParent = teacherOrParent;
    }

    public void setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
    }
}
