package Project.Model.Person;

import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Position.Address;

import java.awt.*;
import java.util.ArrayList;

/**

 * Created by User on 28/8/2559.
 */
public class Student extends Person {
    private TypeOfService typeOfService;
    private String studentId;
    private IsInBus inBus = IsInBus.NO;

    public Student(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address, TypeOfService typeOfService, String studentId) {
        super(role, id, pic, token, tel, user, firstName, surName, faceBookId,address);
        this.typeOfService = typeOfService;
        this.studentId = studentId;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public IsInBus isInBus(){
        return this.inBus;
    }

    public void setInBus(IsInBus inBus){
        this.inBus = inBus;
    }
}
