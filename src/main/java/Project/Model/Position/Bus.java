package Project.Model.Position;

import lombok.Data;

/**
 * Created by User on 28/8/2559.
 */
@Data
public class Bus {
    private int carId;
    private String carNumber;
    private double avgVelocity;
    private int checkPointPassed;
    private double currentLatitude;
    private double currentLongitude;
    private Route currentRoute;
    private String frontImage;
    private String backImage;


    public Bus(int carId, String carNumber, double avgVelocity, int checkPointPassed, double currentLatitude, double currentLongitude, Route currentRoute) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.avgVelocity = avgVelocity;
        this.checkPointPassed = checkPointPassed;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentRoute = currentRoute;
    }

    public Bus(int carId, String carNumber, double avgVelocity, int checkPointPassed) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.avgVelocity = avgVelocity;
        this.checkPointPassed = checkPointPassed;
    }

    public Bus(int carId, String carNumber) {
        this.carId = carId;
        this.carNumber = carNumber;
    }

    public Bus(String carNumber) {
        this.carNumber = carNumber;
    }

    public Bus(int carId) {
        this.carId = carId;
    }

    public Bus(int carId, String carNumber, double currentLatitude, double currentLongitude) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }

    public Bus(int carId, double currentLatitude, double currentLongitude) {
        this.carId = carId;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }

}
