package Project.Model.Person;

import Project.Model.Enumerator.Role;
import Project.Model.Position.Address;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
@Data
public class Teacher extends Person {
    private boolean inBus = false;

    public Teacher(Role role, int id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address) {
        super(role, id, pic, token, tel, user, firstName, surName, faceBookId, address);
    }

}
