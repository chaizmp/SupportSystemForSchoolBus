package Project.Model.Position;

/**
 * Created by User on 1/1/2560.
 */
public class Address {
    private String detail;
    private double latitude;
    private double longitude;

    public Address(String detail, double latitude, double longitude) {
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
}
