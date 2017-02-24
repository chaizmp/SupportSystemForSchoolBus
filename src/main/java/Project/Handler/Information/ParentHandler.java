package Project.Handler.Information;

import Project.Model.Person.Parent;
import Project.Persistent.SQL.ParentPersistent;
import Project.Persistent.SQL.PersonPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class ParentHandler {
    @Autowired
    ParentPersistent parentPersistent;
    @Autowired
    PersonPersistent personPersistent;

    public ArrayList<Parent> getParentsByStudentId(String personId) {
        ArrayList<Parent> parents = parentPersistent.getParentsByStudentId(personId);
        for (Parent it : parents) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        return parents;
    }
}
