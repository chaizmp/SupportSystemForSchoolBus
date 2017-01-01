package Project.Model.Person;

import Project.Model.Enumerator.Role;

import java.awt.*;

/**
 * Created by User on 28/8/2559.
 */
public class Teacher extends Person{
    public Teacher(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId) {
        super(role, id, pic, token, tel, user, firstName, surName, faceBookId);
    }
}
