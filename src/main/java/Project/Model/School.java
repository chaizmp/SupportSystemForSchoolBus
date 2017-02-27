package Project.Model;

import lombok.Data;

/**
 * Created by User on 27/2/2560.
 */
@Data
public class School {
    private String schoolName;
    private double latitude;
    private double longitude;
    private String addressDetail;

    public School(String schoolName, double latitude, double longitude, String addressDetail){
        this.schoolName = schoolName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressDetail = addressDetail;
    }

    public School(){
    }
}
