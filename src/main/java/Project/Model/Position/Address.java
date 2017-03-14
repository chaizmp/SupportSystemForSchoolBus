package Project.Model.Position;

import lombok.Data;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class Address {
    private String detail;
    private double latitude;
    private double longitude;

    public Address(String detail, double latitude, double longitude) {
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
