package Project.Model.Position;

import Project.Model.Enumerator.Type;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class Route {
    private int routeNumber;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private Type type;
    private ArrayList<String> active;
    private ArrayList<Integer> personId;
    private ArrayList<Boolean> temporary;

    public Route(int routeNumber, ArrayList<Double> latitudes, ArrayList<Double> longitudes, Type type) {
        this.routeNumber = routeNumber;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.type = type;
    }

    public Route() {

    }

}
