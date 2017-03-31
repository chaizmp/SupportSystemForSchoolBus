package Project.Model.Person;

import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Position.Address;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
@Data
public class Student extends Person {
    private TypeOfService typeOfService;
    private String studentId;
    private IsInBus inBus = IsInBus.NO;

    public Student(Role role, int id, String image, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<String> address, TypeOfService typeOfService, String studentId) {
        super(role, id, image, token, tel, user, firstName, surName, faceBookId, address);
        this.typeOfService = typeOfService;
        this.studentId = studentId;
    }

}
