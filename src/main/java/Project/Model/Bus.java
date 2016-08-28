package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Bus {
    private String CarNumber;
    private int latitude;
    private int longtitude;
    private ArrayList<Student> students;
    private Teacher teacher;
    private Driver driver;

    public Bus(String carNumber, int latitude, int longtitude, ArrayList<Student> students, Teacher teacher, Driver driver) {
        CarNumber = carNumber;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.students = students;
        this.teacher = teacher;
        this.driver = driver;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(int longtitude) {
        this.longtitude = longtitude;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
