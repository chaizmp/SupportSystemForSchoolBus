package Project.Handler.Information;

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

    public Boolean updateFireBaseToken(String personId, String token) {
        return personPersistent.updateFireBaseToken(personId, token);
    }

    public String getPersonToken(String personId) {
        return personPersistent.getPersonToken(personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(String personId) {
        return personPersistent.getPersonsRelatedToStudent(personId);

    }
}
