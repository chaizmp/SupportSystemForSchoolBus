package Project.Model.Position;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class RouteLog {
    private ArrayList<Route> routes;
    private ArrayList<Date> usedTimes;

    public RouteLog(ArrayList<Route> routes, ArrayList<Date> usedTimes) {
        this.routes = routes;
        this.usedTimes = usedTimes;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ArrayList<Date> getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(ArrayList<Date> usedTimes) {
        this.usedTimes = usedTimes;
    }
}
