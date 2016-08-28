package Project.Model;

/**
 * Created by User on 28/8/2559.
 */
public class Human
{
    private String id;
    private String name;
    private String surName;
    private String telNum;
    private String address;

    public Human()
    {

    }
    public Human(String id, String name, String surName, String telNum, String address) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.telNum = telNum;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getTelNum() {
        return telNum;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
