package Project.Controller;

import Project.Handler.Information.*;
import Project.Handler.JSON.ObjectToJSON;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.Driver;
import Project.Model.Person.Parent;
import Project.Model.Person.Student;
import Project.Model.Person.Teacher;
import Project.Model.Position.Bus;
import Project.Model.School;
import Project.Persistent.SQL.PersonPersistent;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by User on 29/8/2559.
 */
@RestController
public class InformationController {

    @Autowired
    StudentHandler studentHandler;
    @Autowired
    TeacherHandler teacherHandler;
    @Autowired
    ParentHandler parentHandler;
    @Autowired
    BusHandler busHandler;
    @Autowired
    DriverHandler driverHandler;
    @Autowired
    ObjectToJSON objectToJSON;
    @Autowired
    PositionHandler positionHandler;
    @Autowired
    PersonHandler personHandler;
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    SchoolHandler schoolHandler;

    @RequestMapping(value = "setTypeOfService", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean setTypeOfService(@RequestParam(value = "typeOfService") TypeOfService typeOfService,
                             @RequestParam(value = "personId") String personId
    ) {
        return studentHandler.setTypeOfService(typeOfService, personId);
    }

    @RequestMapping(value = "getTeachers", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Teacher> getTeachers(
            @RequestParam(value = "personId") String personId
    ) {
        return teacherHandler.getTeachersByStudentId(personId);
    }

    @RequestMapping(value = "getParents", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Parent> getParents( // method body not yet finished
                                  @RequestParam(value = "personId") String personId
    ) {
        return parentHandler.getParentsByStudentId(personId);
    }

    @RequestMapping(value = "showDriverDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    Driver showDriverDetail( // method body not yet finished
                             @RequestParam(value = "carNumber") String carNumber //teacher or parent
    ) {
        return null; // returns all students, their teacher (if id is from parent) / their parent (if id is from  teacher)
        // and Bus where they are  ( with button to show the drivers)

    }

    @RequestMapping(value = "getAllStudentInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Student> getAllStudent(
            @RequestParam(value = "personId") String personId //teacher or parent
    ) {
        return studentHandler.getAllStudentByPersonId(personId);
    }

    @RequestMapping(value = "getStudentInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    Student getStudent(
            @RequestParam(value = "personId") String personId //personId of a student
    ) {
        return studentHandler.getStudentByPersonId(personId);
    }

    @RequestMapping(value = "getAllPassengerInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    String getPassenger(
            @RequestParam(value = "carNumber") String carNumber //personId of a driver
    ) {
        ArrayList<Student> students = studentHandler.getCurrentAllStudentByCarNumber(carNumber);
        ArrayList<Teacher> teachers = teacherHandler.getCurrentAllTeacherByCarNumber(carNumber);
        JSONObject studentsJSON = objectToJSON.arrayListToJSON("students", students);
        JSONObject teachersJSON = objectToJSON.arrayListToJSON("teachers", teachers);
        JSONObject result = objectToJSON.mergeJSONObjects(studentsJSON, teachersJSON);
        if (result != null) {
            return result.toString();
        }
        return null;
    }

    @RequestMapping(value = "addStudentsTrip", method = RequestMethod.POST)
    boolean addStudentsTrip(
                    @RequestParam(value = "personIds") ArrayList<String> personIds,
                    @RequestParam(value = "carNumber") String carNumber
            ){
        return studentHandler.addStudentsTrip(personIds, carNumber);
    }

    @RequestMapping(value = "getAllStudentInBus", method = RequestMethod.POST)
    public
    @ResponseBody
    String getAllStudentInBus(
            @RequestParam(value = "carNumber") String carNumber //personId of a driver
    ) {
        ArrayList<Student>  students = studentHandler.getCurrentAllStudentByCarNumber(carNumber);
        for (Student it : students) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        boolean isEvery = studentHandler.isEveryStudentGetsOnTheBus(carNumber);
        String isEveryResult = "{ \"isEvery\":"+isEvery+"}";
        JSONObject studentsJSON = objectToJSON.arrayListToJSON("students", students);
        JSONObject result = objectToJSON.mergeJSONObjects(studentsJSON, new JSONObject(isEveryResult));
        if(result != null) {
            return result.toString();
        }
        return null;
    }

    @RequestMapping(value = "getSchoolDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    School getSchoolDetail(
            @RequestParam(value = "schoolName") String schoolName
    ) {
        return schoolHandler.getSchoolDetail(schoolName);
    }

    @RequestMapping(value = "getBusDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject getBusDetail(
            @RequestParam(value = "personId") String personId
    ) {
        Bus bus = busHandler.getCurrentBusCarNumberByStudentId(personId); //latest time in the log
        ArrayList<Timestamp> startAndEndPeriod = positionHandler.getCurrentStartAndEndPeriodByStudentId(bus.getCarNumber(), personId);
        ArrayList<Teacher> teachers = teacherHandler.getCurrentTeacherInBusByCarNumber(bus.getCarNumber(), startAndEndPeriod); // between that trip
        Driver driver = driverHandler.getCurrentDriverInBusByCarNumber(bus.getCarNumber(), startAndEndPeriod); //there is always one driver
        JSONObject busJSON = objectToJSON.mapToJSON("bus", bus);
        JSONObject teachersJSON = objectToJSON.arrayListToJSON("teachers", teachers);
        JSONObject driverJSON = objectToJSON.mapToJSON("driver", driver);
        JSONObject result = objectToJSON.mergeJSONObjects(busJSON, teachersJSON, driverJSON);
        if (result != null) {
            return result;
        }
        return null;
    }

    @RequestMapping(value = "updateFireBaseToken", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean updateFireBaseToken(
            @RequestParam(value = "personId") String personId,
            @RequestParam(value = "token") String token
    ) {
        return personHandler.updateFireBaseToken(personId, token);
    }

}
