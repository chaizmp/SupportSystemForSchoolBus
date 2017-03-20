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

    public Teacher(Role role, int id, String image, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address) {
        super(role, id, image, token, tel, user, firstName, surName, faceBookId, address);
    }

}
