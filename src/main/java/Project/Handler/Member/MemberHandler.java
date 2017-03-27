package Project.Handler.Member;

import Project.Handler.Information.PersonHandler;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import Project.Persistent.SQL.ParentPersistent;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.StudentPersistent;
import Project.Persistent.SQL.TeacherPersistent;
import Project.Utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by User on 5/3/2560.
 */
@Service
public class MemberHandler {
    @Autowired
    AuthenticationUtil authenticationUtil;
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    ParentPersistent parentPersistent;
    @Autowired
    TeacherPersistent teacherPersistent;
    @Autowired
    PersonHandler personHandler;
    @Autowired
    StudentPersistent studentPersistent;

    public boolean signUp(String username, String password, String name, String surname, Role role, String tel, ArrayList<Double> latitudes, ArrayList<Double> longitudes, ArrayList<String> details, String image){

        byte[] salt = authenticationUtil.generateSalt();
        Base64.Encoder enc = Base64.getEncoder();
        String hash = authenticationUtil.hash(password, salt);
        int mainTableResult = personPersistent.addPerson(username, hash, enc.encodeToString(salt), name, surname, role, tel);
        //***************** get that key
        if(mainTableResult != -1){
            if(latitudes != null) {
                personHandler.addPersonAddressById(mainTableResult, latitudes, longitudes, details);
            }
            if(image != null) {
                personHandler.savePersonImage(image, mainTableResult);
            }
            switch (role) {
                case PARENT:
                    parentPersistent.addParent(mainTableResult);
                    break;
                case TEACHER:
                    teacherPersistent.addTeacher(mainTableResult);
                    break;
                default:
                    break;
            }
        }
        return mainTableResult != -1;
    }

    public boolean studentSignUp(String studentId, String name, String surname, String tel, ArrayList<Double> latitudes, ArrayList<Double> longitudes, ArrayList<String> details, TypeOfService typeOfService, String image){

        int mainTableResult = personPersistent.addPerson(null, null, null, name, surname, Role.STUDENT, tel);
        //***************** get that key
        if(mainTableResult != -1){
            if(latitudes != null) {
                personHandler.addPersonAddressById(mainTableResult, latitudes, longitudes, details);
            }
            if(image != null) {
                personHandler.savePersonImage(image, mainTableResult);
            }
            studentPersistent.addStudent(studentId, typeOfService, mainTableResult);
        }
        return mainTableResult != -1;
    }

    public boolean signIn(String username, String password){
        String salt = personPersistent.getSalt(username);
//        Base64.Encoder enc = Base64.getEncoder();
        Base64.Decoder dec = Base64.getDecoder();
        String newHash = authenticationUtil.hash(password, dec.decode(salt));
        String trueHash = personPersistent.getPassword(username);
        return newHash.equals(trueHash);
    }
}
