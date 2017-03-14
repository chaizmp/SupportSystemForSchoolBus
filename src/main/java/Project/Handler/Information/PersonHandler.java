package Project.Handler.Information;

import Project.Model.Enumerator.Role;
import Project.Model.Person.Person;
import Project.Persistent.SQL.PersonPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by User on 20/2/2560.
 */
@Service
public class PersonHandler {
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    TeacherHandler teacherHandler;

    public Boolean updateFireBaseToken(int personId, String token) {
        return personPersistent.updateFireBaseToken(personId, token);
    }

    public String getPersonToken(int personId) {
        return personPersistent.getPersonToken(personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(int personId) {
        return personPersistent.getPersonsRelatedToStudent(personId);

    }

    public boolean addPersonAddressById(int personId, ArrayList<Double> latitudes, ArrayList<Double> longitudes, ArrayList<String> details){
        boolean result = true;
        for(int i = 0; i< latitudes.size(); i++){
            if(!personPersistent.addPersonAddressByPersonId(personId, latitudes.get(i), longitudes.get(i))) {
                result = false;
            }
            if(!personPersistent.addAddressDetail(latitudes.get(i),longitudes.get(i), details.get(i))){
                result = false;
            }
        }
        return result;
    }

    public Person getPersonByPersonId(int personId){
        return personPersistent.getPersonByPersonId(personId);
    }

    public boolean setAlarm(int personId, int duration){
        return personPersistent.setAlarm(personId, duration);
    }

    public int getPersonAlarm(int personId){
        return personPersistent.getPersonAlarm(personId);
    }

    public String getRoleByUsername(String username){
       return personPersistent.getRoleByUsername(username);
    }
    public int getPersonIdByUsername(String username){
        return personPersistent.getPersonIdByUsername(username);
    }
}
