package Project.Model.Log;

import Project.Model.Person.Person;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class BusLog {
    private ArrayList<Person> persons;
    private ArrayList<String> busIds;
    private ArrayList<Date> times;
    private ArrayList<Integer> enterTime;
    private ArrayList<Boolean> eachIsInBus;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    public BusLog(ArrayList<Person> persons, ArrayList<String> busIds, ArrayList<Date> times, ArrayList<Integer> enterTime, ArrayList<Boolean> eachIsInBus, ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
        this.persons = persons;
        this.busIds = busIds;
        this.times = times;
        this.enterTime = enterTime;
        this.eachIsInBus = eachIsInBus;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<String> getBusIds() {
        return busIds;
    }

    public void setBusIds(ArrayList<String> busIds) {
        this.busIds = busIds;
    }

    public ArrayList<Date> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Date> times) {
        this.times = times;
    }

    public ArrayList<Integer> getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(ArrayList<Integer> enterTime) {
        this.enterTime = enterTime;
    }

    public ArrayList<Boolean> getEachIsInBus() {
        return eachIsInBus;
    }

    public void setEachIsInBus(ArrayList<Boolean> eachIsInBus) {
        this.eachIsInBus = eachIsInBus;
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
