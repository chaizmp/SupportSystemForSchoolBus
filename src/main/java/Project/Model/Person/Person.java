package Project.Model.Person;

import Project.Model.Enumerator.Role;
import Project.Model.Position.Address;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
@Data
public class Person {
    private Role role;
    private int id;
    private Image pic;
    private String token;
    private String tel;
    private String user;
    private String firstName;
    private String surName;
    private String faceBookId;
    private ArrayList<Address> addresses;

    public Person(Role role, int id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> addresses) {
        this.role = role;
        this.id = id;
        this.pic = pic;
        this.token = token;
        this.tel = tel;
        this.user = user;
        this.firstName = firstName;
        this.surName = surName;
        this.faceBookId = faceBookId;
        this.addresses = addresses;
    }
}
