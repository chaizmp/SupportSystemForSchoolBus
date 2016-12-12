package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Bus {
    private String carNumber;
    private ArrayList<Integer>[] position = new ArrayList[2];
    private ArrayList<Integer> latitude;
    private ArrayList<Integer> longitude;
    private ArrayList<Float> velocity;
//    private ArrayList<Student> students;
    private Teacher teacher;
    private Driver driver;

    public Bus(String carNumber) {
          this.carNumber = carNumber;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.velocity = velocity;
        //this.teacher = teacher;
        //this.driver = driver;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        carNumber = carNumber;
    }

    public ArrayList<Integer> getLatitude() {
        return latitude;
    }

    public void setLatitude(ArrayList<Integer> latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Integer> getLongitude() {
        return longitude;
    }

    public void setLongitude(ArrayList<Integer> longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Float> getVelocity() {
        return velocity;
    }

    public void setVelocity(ArrayList<Float> velocity) {
        this.velocity = velocity;
    }
//
//    public ArrayList<Student> getStudents() {
//        return students;
//    }

//    public void setStudents(ArrayList<Student> students) {
//        this.students = students;
//    }

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

    public ArrayList<Integer>[] getPosition() {
        return position;
    }

    public void setPosition(ArrayList<Integer>[] position) {
        this.position = position;
    }
}
