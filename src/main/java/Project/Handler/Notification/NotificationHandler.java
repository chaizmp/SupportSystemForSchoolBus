package Project.Handler.Notification;

import Project.Handler.ApiCalls.ApiCall;
import Project.Handler.Information.PersonHandler;
import Project.Model.Notification.NotificationForm;
import Project.Model.Notification.NotificationMessage;
import Project.Model.Person.Person;
import Project.Model.Person.Student;
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
    public ArrayList<String> arrival(int personId, String carNumber, boolean isHome){
        ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId); // there will be pending db
        ArrayList<String> responseList = new ArrayList<>();
        NotificationMessage notificationMessage = new NotificationMessage();
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTitle("Support System For School Bus");
        if(isHome) {
            notificationForm.setBody("The Bus Number " + carNumber + " just arrived at your student home.");
        }else{
            notificationForm.setBody("The Bus Number " + carNumber + " just arrived at the school.");
        }
        notificationMessage.setNotification(notificationForm);
        for (Person it : userRelatedToStudent) {
            notificationMessage.setTo(it.getToken());
            responseList.add(apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage));
        }
        return responseList;
    }

    public ArrayList<String> notifyBus(ArrayList<Integer> personIds, String carNumber, String message){
        ArrayList<String> responseList = new ArrayList<>();
        NotificationMessage notificationMessage = new NotificationMessage();
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTitle("Support System For School Bus");
        notificationForm.setBody(carNumber+": "+message);
        notificationMessage.setNotification(notificationForm);
        for(int personId: personIds) {
            ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId); // there will be pending db
            for (Person it : userRelatedToStudent) {
                notificationMessage.setTo(it.getToken());
                responseList.add(apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage));
            }
        }
        return responseList;
    }

    public ArrayList<String> alarm(String carNumber, String token, String firstName, String surname){
        if(token != null) {
            ArrayList<String> responseList = new ArrayList<>();
            NotificationMessage notificationMessage = new NotificationMessage();
            NotificationForm notificationForm = new NotificationForm();
            notificationForm.setTitle("Support System For School Bus");
            notificationForm.setBody(carNumber + ": your child(" + firstName + " " + surname + ") took will arrive at home in few minutes");
            notificationMessage.setNotification(notificationForm);
            notificationMessage.setTo(token);
            responseList.add(apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage));
            return responseList;
        }
        return null;
    }

}
