package Project.Model.Position;

import Project.Model.Person.Person;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Bus {
    private String carNumber;
    private double avgVelocity;
    private int checkPointPassed;
    private double currentLatitude;
    private double currentLongitude;
    private Route currentRoute;


    public Bus(String carNumber, double avgVelocity, int checkPointPassed, double currentLatitude, double currentLongitude, Route currentRoute) {
        this.carNumber = carNumber;
        this.avgVelocity = avgVelocity;
        this.checkPointPassed = checkPointPassed;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentRoute = currentRoute;
    }

    public Bus(String carNumber, double avgVelocity, int checkPointPassed){
        this.carNumber = carNumber;
        this.avgVelocity = avgVelocity;
        this.checkPointPassed = checkPointPassed;
    }

    public Bus(String carNumber){
        this.carNumber = carNumber;
    }

    public Bus(String carNumber, double currentLatitude, double currentLongitude){
        this.carNumber = carNumber;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public double getAvgVelocity() {
        return avgVelocity;
    }

    public void setAvgVelocity(double avgVelocity) {
        this.avgVelocity = avgVelocity;
    }

    public int getCheckPointPassed() {
        return checkPointPassed;
    }

    public void setCheckPointPassed(int checkPointPassed) {
        this.checkPointPassed = checkPointPassed;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public void setCurrentRoute(Route currentRoute) {this.currentRoute = currentRoute;}

    public Route setCurrentRoute() {return currentRoute;}

    public Route getCurrentRoute() {return currentRoute;}
}
