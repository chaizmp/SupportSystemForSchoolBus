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
    public ArrayList<String> arrival(String personId, String carNumber, boolean isHome){
        ArrayList<Person> userRelatedToStudent = personHandler.getPersonsRelatedToStudent(personId);
        ArrayList<String> responseList = new ArrayList<>();
        for (Person it : userRelatedToStudent) {
            NotificationMessage notificationMessage = new NotificationMessage();
            NotificationForm notificationForm = new NotificationForm();
            notificationForm.setTitle("Support System For School Bus");
            if(isHome) {
                notificationForm.setBody("The Bus Number " + carNumber + " just arrived at your student home.");
            }else{
                notificationForm.setBody("The Bus Number " + carNumber + " just arrived at the school.");
            }
            notificationMessage.setNotificationForm(notificationForm);
            notificationMessage.setTo(it.getToken());
            responseList.add(apiCall.sendGetOnOrOffNotificationToPersons(notificationMessage));
        }
        return responseList;
    }

}
