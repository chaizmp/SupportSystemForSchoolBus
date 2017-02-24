package Project.Model.Person;

import Project.Model.Enumerator.Role;
import Project.Model.Position.Address;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by User on 1/1/2560.
 */
public class Person {
    private Role role;
    private String id;
    private Image pic;
    private String token;
    private String tel;
    private String user;
    private String firstName;
    private String surName;
    private String faceBookId;
    private ArrayList<Address> addresses;

    public Person(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId,ArrayList<Address> addresses) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFaceBookId() {
        return faceBookId;
    }

    public void setFaceBookId(String faceBookId) {
        this.faceBookId = faceBookId;
    }

    public void setAddresses(ArrayList<Address> addresses) {this.addresses = addresses;}

    public ArrayList<Address> getAddresses() {return addresses;}
}
