package Project.Handler.Notification;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.*;
import Project.Model.Enumerator.IsInBus;
import Project.Model.Notification.NotificationForm;
import Project.Model.Notification.NotificationMessage;
import Project.Model.Person.Driver;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
import Project.Model.Person.Teacher;
import Project.Persistent.SQL.PositionPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by User on 27/2/2560.
 */
@Service
public class NotificationHandler {
    @Autowired
    PersonHandler personHandler;
    @Autowired
    ApiCall apiCall;
    @Autowired
    StudentHandler studentHandler;
    @Autowired
    PositionPersistent positionPersistent;
    @Autowired
    DriverHandler driverHandler;
    @Autowired
    TeacherHandler teacherHandler;
    @Autowired
    BusHandler busHandler;
    public ArrayList<String> arrival(int personId, String carNumber, boolean isHome){
        if(!isHome) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                            System.out.println("TEST");
                            if (positionPersistent.isInBus(personId) == IsInBus.YES) {
                                Student student = studentHandler.getStudentByPersonId(personId);
                                NotificationMessage dangerMessage = new NotificationMessage();
                                NotificationForm form = new NotificationForm();
                                form.setTitle("Danger Alert");
                                int carId = busHandler.getLatestBusCarIdByStudentId(personId).getCarId();
                                Driver driver = driverHandler.getLatestDriverInBusByCarId(carId);
                                String body ="";
                                if(driver == null) {
                                    body = "Please contact ";
                                }else{
                                    body = "Please contact " + driver.getFirstName() + " (driver) " + driver.getTel();
                                }
                                ArrayList<Teacher> teacher = teacherHandler.getCurrentAllTeacherByCarId(carId);
                                for(Teacher it: teacher){
                                    body +="\n"+it.getFirstName()+"(teacher) "+it.getTel();
                                }
                                form.setBody(student.getFirstName()+" "+student.getSurName()+" is still in bus number"+carNumber+"\n" +
                                        body );
                                dangerMessage.setNotification(form);
                                ArrayList<Person> theirParentsAndTeachers = personHandler.getPersonsRelatedToStudent(personId);
                                for(Person it: theirParentsAndTeachers){
                                    dangerMessage.setTo(it.getToken());
                                    apiCall.sendGetOnOrOffNotificationToPersons(dangerMessage);
                                }
                            }
                        }
                    },
                    600000
            );
        }
        ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId); // there will be pending db
        ArrayList<String> responseList = new ArrayList<>();
        NotificationMessage notificationMessage = new NotificationMessage();
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTitle("Arrival");
        Student student = studentHandler.getStudentByPersonId(personId);
        if(isHome) {
            notificationForm.setBody("("+student.getFirstName()+" "+student.getSurName()+") The Bus Number " + carNumber + " just arrived at your student home.");
        }else{
            notificationForm.setBody("("+student.getFirstName()+" "+student.getSurName()+") The Bus Number " + carNumber + " just arrived at the school.");
        }
        notificationMessage.setNotification(notificationForm);
        for (Person it : userRelatedToStudent) {
            notificationMessage.setTo(it.getToken());
            apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);
            //responseList.add();
        }
        return responseList;
    }

    public ArrayList<String> notifyBus(ArrayList<Integer> personIds, String carNumber, String message){
        System.out.println("NOTI FY all");
        for(int it: personIds){
            System.out.println(it);
        }
        ArrayList<String> responseList = new ArrayList<>();
        NotificationMessage notificationMessage = new NotificationMessage();
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTitle("Message From Bus");
        notificationForm.setBody(carNumber+": "+message);
        notificationMessage.setNotification(notificationForm);
        for(int personId: personIds) {
            ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId); // there will be pending db
            for (Person it : userRelatedToStudent) {
                notificationMessage.setTo(it.getToken());
                apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);
                //responseList.add();
            }
        }
        return responseList;
    }

    public ArrayList<String> alarm(String carNumber, String token, String firstName, String surname){
        if(token != null) {
            ArrayList<String> responseList = new ArrayList<>();
            NotificationMessage notificationMessage = new NotificationMessage();
            NotificationForm notificationForm = new NotificationForm();
            notificationForm.setTitle("Alarm");
            notificationForm.setBody(carNumber + ": your child(" + firstName + " " + surname + ") took will arrive at home in few minutes");
            notificationMessage.setNotification(notificationForm);
            notificationMessage.setTo(token);
            apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage);
            //responseList.add();
            //return responseList;
        }
        return null;
    }

}
