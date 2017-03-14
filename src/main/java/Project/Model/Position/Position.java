package Project.Model.Position;

import Project.Model.Enumerator.Status;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by User on 1/1/2560.
 */
@Data
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

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
