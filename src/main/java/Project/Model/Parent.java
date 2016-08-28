package Project.Model;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Parent extends Human{
    private ArrayList<Student> children;

    public Parent(String id, String name, String surName, String telNum, String address, ArrayList<Student> children) {
        super(id, name, surName, telNum, address);
        this.children = children;
    }

    public Parent(String id, String name, String surName, String telNum, String address) {
        super(id, name, surName, telNum, address);
    }

    public ArrayList<Student> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Student> children) {
        this.children = children;
    }
}
