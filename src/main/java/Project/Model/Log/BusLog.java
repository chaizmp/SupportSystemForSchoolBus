package Project.Model.Log;

import Project.Model.Person.Person;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class BusLog {
    private ArrayList<Person> persons;
    private ArrayList<Integer> busIds;
    private ArrayList<Date> times;
    private ArrayList<Integer> enterTime;
    private ArrayList<Boolean> eachIsInBus;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    public BusLog(ArrayList<Person> persons, ArrayList<Integer> busIds, ArrayList<Date> times, ArrayList<Integer> enterTime, ArrayList<Boolean> eachIsInBus, ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
        this.persons = persons;
        this.busIds = busIds;
        this.times = times;
        this.enterTime = enterTime;
        this.eachIsInBus = eachIsInBus;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
    }

}
