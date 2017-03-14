package Project.Model.Position;

import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class RouteLog {
    private ArrayList<Route> routes;
    private ArrayList<Date> usedTimes;

    public RouteLog(ArrayList<Route> routes, ArrayList<Date> usedTimes) {
        this.routes = routes;
        this.usedTimes = usedTimes;
    }
}
