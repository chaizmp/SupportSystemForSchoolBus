package Project.Model.Position;

import Project.Model.Enumerator.Status;

import java.sql.Timestamp;

/**
 * Created by User on 1/1/2560.
 */
public class Position {
    private double latitude;
    private double longitude;
    private Status status;
    private Timestamp timestamp;

    public Position(double latitude, double longitude, Status status, Timestamp timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTime(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
