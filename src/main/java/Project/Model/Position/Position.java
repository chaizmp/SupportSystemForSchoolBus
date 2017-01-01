package Project.Model.Position;

import java.sql.Date;

/**
 * Created by User on 1/1/2560.
 */
public class Position {
    private double latitude;
    private double longitude;
    private boolean isPark;
    private Date time;

    public Position(double latitude, double longitude, boolean isPark, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isPark = isPark;
        this.time = time;
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

    public boolean isPark() {
        return isPark;
    }

    public void setPark(boolean park) {
        isPark = park;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
