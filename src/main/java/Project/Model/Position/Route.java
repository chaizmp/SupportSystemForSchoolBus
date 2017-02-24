package Project.Model.Position;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class Route {
    private int routeNumber;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    public Route(int routeNumber, ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
        this.routeNumber = routeNumber;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
    }

    public Route() {

    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public ArrayList<Double> getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(ArrayList<Double> latitudes) {
        this.latitudes = latitudes;
    }

    public ArrayList<Double> getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(ArrayList<Double> longitudes) {
        this.longitudes = longitudes;
    }
}
