package Project.Controller;

import Project.Handler.Information.*;
import Project.Handler.JSON.ObjectToJSON;
import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Enumerator.TypeOfService;
import Project.Model.Person.*;
import Project.Model.Position.Bus;
import Project.Model.School;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.PositionPersistent;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;


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
    @Autowired
    PositionPersistent positionPersistent;

    @RequestMapping(value = "saveCamera", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean saveCamera(@RequestParam(value = "carId") int carId,
                             @RequestParam(value = "image") String image,
                             @RequestParam(value = "isFront") boolean isFront
    ) {
        Base64.Decoder dec = Base64.getDecoder();
        return busHandler.saveCameraImage(carId, dec.decode(image), isFront);
    }

    @RequestMapping(value = "getImageFromBus", method = RequestMethod.POST)
    public
    @ResponseBody
    String getImageFromBus(@RequestParam(value = "carId") int carId
    ) {
        JSONObject imageJSON = new JSONObject();
        imageJSON.put("imageFront",busHandler.getImageFromBus(carId, true));
        imageJSON.put("imageBack", busHandler.getImageFromBus(carId, false));
        return imageJSON.toString();
    }

    @RequestMapping(value = "setTypeOfService", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean setTypeOfService(@RequestParam(value = "typeOfService") TypeOfService typeOfService,
                             @RequestParam(value = "personId") int personId
    ) {
        return studentHandler.setTypeOfService(typeOfService, personId);
    }

    @RequestMapping(value = "getTeachers", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Teacher> getTeachers(
            @RequestParam(value = "personId") int personId
    ) {
        return teacherHandler.getTeachersByStudentId(personId);
    }

    @RequestMapping(value = "getTeacherById", method = RequestMethod.POST)
    public
    @ResponseBody
    Person getPersonByPersonId(
            @RequestParam(value = "personId") int personId
    ) {
        Person person =  personHandler.getPersonByPersonId(personId);
        person.setAddresses(personPersistent.getPersonAddressesByPersonId(personId));
        return person;
    }

    @RequestMapping(value = "getParents", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Parent> getParents( // method body not yet finished
                                  @RequestParam(value = "personId") int personId
    ) {
        return parentHandler.getParentsByStudentId(personId);
    }

    @RequestMapping(value = "showDriverDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    Driver showDriverDetail( // method body not yet finished
                             @RequestParam(value = "carId") int carId//teacher or parent
    ) {
        return null; // returns all students, their teacher (if id is from parent) / their parent (if id is from  teacher)
        // and Bus where they are  ( with button to show the drivers)

    }

    @RequestMapping(value = "getAllStudentInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Student> getAllStudent(
            @RequestParam(value = "personId") int personId, //teacher or parent
            @RequestParam(value = "needImage") boolean needImage
    ) {
        return studentHandler.getAllStudentByPersonId(personId, needImage);
    }

    @RequestMapping(value = "getStudentInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    Student getStudent(
            @RequestParam(value = "personId") int personId //personId of a student
    ) {
        return studentHandler.getStudentByPersonId(personId);
    }

    @RequestMapping(value = "getStudentInformationWeb", method = RequestMethod.POST)
    public
    @ResponseBody
    Student getStudentWeb(
            @RequestParam(value = "personId") int personId //personId of a student
    ) {
        Student s = studentHandler.getStudentByPersonId(personId);
        s.setImage(personHandler.getPersonImage(personId));
        return s;
    }

    @RequestMapping(value = "getPersons", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Person> getPersons(
    ) {
        ArrayList<Person> s = personHandler.getAllPersonsExceptStudents();
        return s;
    }

    @RequestMapping(value = "getAllPassengerInformation", method = RequestMethod.POST)
    public
    @ResponseBody
    String getPassenger(
            @RequestParam(value = "carId") int carId, //personId of a driver
            @RequestParam(value = "needImage") boolean needImage
    ) {
        //ArrayList<Student> students = studentHandler.getCurrentAllStudentByCarId(carId);
        ArrayList<Student> students = studentHandler.getAllStudentInCurrentTrip(carId, needImage);
        Driver driver = driverHandler.getLatestDriverInBusByCarId(carId);
        if(driver != null) {
            driver.setAddresses(personPersistent.getPersonAddressesByPersonId(driver.getId()));
            if(needImage){
                driver.setImage(personHandler.getPersonImage(driver.getId()));
            }
        }
        ArrayList<Teacher> teachers = teacherHandler.getCurrentAllTeacherByCarId(carId);
        for(Teacher it: teachers){
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
            if(needImage) {
                it.setImage(personHandler.getPersonImage(it.getId()));
            }
        }
        JSONObject studentsJSON = objectToJSON.arrayListToJSON("students", students);
        JSONObject teachersJSON = objectToJSON.arrayListToJSON("teachers", teachers);
        JSONObject driverJSON = objectToJSON.mapToJSON("driver", driver);
        JSONObject result = objectToJSON.mergeJSONObjects(studentsJSON, teachersJSON, driverJSON);
        if (result != null) {
            return result.toString();
        }
        return null;
    }

    @RequestMapping(value = "addStudentsTrip", method = RequestMethod.POST)
    boolean addStudentsTrip( //synchronization problem
                    @RequestParam(value = "personIds") ArrayList<Integer> personIds,
                    @RequestParam(value = "carId") int carId
            ){
        return studentHandler.addStudentsTrip(personIds, carId);
    }

    @RequestMapping(value = "getAllStudentInBus", method = RequestMethod.POST)
    public
    @ResponseBody
    String getAllStudentInBus(
            @RequestParam(value = "carId") int carId//personId of a driver
    ) {
        //ArrayList<Student>  students = studentHandler.getCurrentAllStudentByCarId(carId);
        ArrayList<Student> students = studentHandler.getAllStudentInCurrentTrip(carId, false);
        for (Student it : students) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        boolean isEvery = studentHandler.isEveryStudentGetsOnTheBus(carId);
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

    @RequestMapping(value = "getPerson", method = RequestMethod.POST)
    public
    @ResponseBody
    Person getPerson(
            @RequestParam(value = "personId") int personId
    ) {
        Person p = personHandler.getPersonByPersonId(personId);
        p.setAddresses(personPersistent.getPersonAddressesByPersonId(personId));
        p.setImage(personHandler.getPersonImage(personId));
        return p;
    }

    @RequestMapping(value = "getStudentsByTeacherOrParent", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Student> getStudentsByTeacherOrParent(
            @RequestParam(value = "personId") int personId
    ) {
        return studentHandler.getAllStudentByPersonId(personId, false);
    }


    @RequestMapping(value = "getBusDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    String getBusDetail(
            @RequestParam(value = "personId") int personId
    ) {
        //Bus bus = busHandler.getCurrentBusCarIdByStudentId(personId);//latest time in the log
        Bus bus = busHandler.getLatestBusCarIdByStudentId(personId);
        if(bus != null) {
            bus.setCarNumber(busHandler.getBusCarNumberByCarId(bus.getCarId()));
            ArrayList<Timestamp> startAndEndPeriod = positionHandler.getCurrentStartAndEndPeriodByStudentId(bus.getCarId(), personId);
            ArrayList<Teacher> teachers = teacherHandler.getCurrentTeacherInBusByCarId(bus.getCarId(), startAndEndPeriod); // between that trip
            Student student = studentHandler.getStudentByPersonId(personId);
            student.setAddresses(personPersistent.getPersonAddressesByPersonId(personId));
            Driver driver = driverHandler.getCurrentDriverInBusByCarId(bus.getCarId(), startAndEndPeriod); //there is always one driver
            JSONObject studentJSON = objectToJSON.mapToJSON("student", student);
            JSONObject busJSON = objectToJSON.mapToJSON("bus", bus);
            JSONObject teachersJSON = objectToJSON.arrayListToJSON("teachers", teachers);
            JSONObject driverJSON = objectToJSON.mapToJSON("driver", driver);
            JSONObject result = objectToJSON.mergeJSONObjects(busJSON, teachersJSON, driverJSON, studentJSON);
            if(result != null){
                return result.toString();
            }
        }
        return "-1";
    }

    @RequestMapping(value = "updateFireBaseToken", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean updateFireBaseToken(
            @RequestParam(value = "personId") int personId,
            @RequestParam(value = "token") String token
    ) {
        return personHandler.updateFireBaseToken(personId, token);
    }

    @RequestMapping(value = "addParentAndStudentRelationships", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean addParentAndStudentRelationships(
            @RequestParam(value = "personPId") int personPId,
            @RequestParam(value = "personSIds") ArrayList<Integer> personSIds
    ) {
        return parentHandler.addParentAndStudentRelationships(personPId, personSIds);
    }

    @RequestMapping(value = "addTeacherAndStudentRelationships", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean addTeacherAndStudentRelationships(
            @RequestParam(value = "personTId") int personPId,
            @RequestParam(value = "personSIds") ArrayList<Integer> personSIds,
            @RequestParam(value = "classRoomName") String classRoomName
    ) {
        return teacherHandler.addTeacherAndStudentRelationships(personPId, personSIds, classRoomName);
    }

    @RequestMapping(value = "getAllStudentInCurrentTrip", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Student> getAllStudentInCurrentTrip(
        @RequestParam(value = "carId") int carId
    ){
        return studentHandler.getAllStudentInCurrentTrip(carId, false);
    }

    @RequestMapping(value = "getAllParents", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Person> getAllParents(
    ){
        return parentHandler.getAllParents();
    }

    @RequestMapping(value = "getAllTeachers", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<Person> getAllTeachers(
    ){
        return teacherHandler.getAllTeachers();
    }

    @RequestMapping(value = "getPersonImage", method = RequestMethod.POST)
    public
    @ResponseBody
    String getPersonImage(
            @RequestParam(value = "personId") int personId
    ){
        JSONObject result = new JSONObject();
        String image = personHandler.getPersonImage(personId);
        result.put("personId",personId);
        result.put("image",image);
        return result.toString();
    }

    @RequestMapping(value= "cancelStudentTrip", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean cancelStudentTrip(
            @RequestParam(value = "personIds") ArrayList<Integer> personIds
    ){
        return studentHandler.cancelStudentTrip(personIds);
    }

    @RequestMapping(value= "clearTrip", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean clearTrip(
    ){
        return positionHandler.clearTrip();
    }

    @RequestMapping(value= "testVelo", method = RequestMethod.POST)
    public
    @ResponseBody
    double testVelo(
            @RequestParam(value = "time1") Timestamp time1,
            @RequestParam(value = "time2") Timestamp time2,
            @RequestParam(value = "lat1") double lat1,
            @RequestParam(value = "lon1") double lon1,
            @RequestParam(value = "lat2") double lat2,
            @RequestParam(value = "lon2") double lon2
    ) {
        return positionHandler.haverSineDistance(lat1,lon1,lat2,lon2)/((time2.getTime() - time1.getTime())/1000);

    }

    @RequestMapping(value= "deletePerson", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean deletePerson(
            @RequestParam(value = "personId") int personId
    ) {
        return personHandler.deletePersonByPersonId(personId);
    }

}

