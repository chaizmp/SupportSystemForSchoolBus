package Project.Controller;

import Project.Handler.Information.PersonHandler;
import Project.Handler.JSON.ObjectToJSON;
import Project.Handler.Member.MemberHandler;
import Project.Model.Enumerator.Role;
import Project.Model.Enumerator.TypeOfService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by User on 29/8/2559.
 */
@RestController
public class MemberController {
    @Autowired
    MemberHandler memberHandler;
    @Autowired
    PersonHandler personHandler;
    @Autowired
    ObjectToJSON objectToJSON;

    @RequestMapping(value = "signOnWithFb", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and method body not yet finished
    String signOnWithFacebook
    (
            @RequestParam(value = "facebookId") String faceBookId
    ) {
        return new String("hehe");
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and param list and body not yet finished
    String register
    (
            @RequestParam(value = "userName", required = false) String userName
    ) {
        return new String("hehehe");
    }

    @RequestMapping(value = "signUp", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and param list and body not yet finished
    boolean signUp
    (
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "role") Role role,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "tel") String tel,
            @RequestParam(value = "details") ArrayList<String> details,
            @RequestParam(value = "image") String image
            ) {
        boolean result = memberHandler.signUp(username, password, name, surname, role, tel, details, image);
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = "signIn", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and param list and body not yet finished
    String logIn
    (
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password

    ){
        boolean result =  memberHandler.signIn(username, password);
        if(result){
            JSONObject role = new JSONObject(/*"{\"role\": \""+personHandler.getRoleByUsername(username)+"\"}"*/);
            role.put("role", personHandler.getRoleByUsername(username));
            JSONObject personId = new JSONObject(/*"{\"personId\": \""+personHandler.getPersonIdByUsername(username)+"\"}"*/);
            personId.put("personId", personHandler.getPersonIdByUsername(username));
            JSONObject resultJSON = objectToJSON.mergeJSONObjects(personId,role);
            return resultJSON.toString();
        }
        return null;
    }

    @RequestMapping(value = "addStudent", method = RequestMethod.POST)
    public
    @ResponseBody
        // return type and param list and body not yet finished
    boolean addStudent
    (
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "tel") String tel,
            @RequestParam(value = "studentId") String studentId,
            @RequestParam(value = "typeOfService") TypeOfService typeOfService,
            @RequestParam(value = "details") ArrayList<String> details,
            @RequestParam(value=  "image") String image
    ){
        boolean result =memberHandler.studentSignUp(studentId, name, surname, tel, details, typeOfService, image);
        System.out.println(result);
        return  result;
    }
}
