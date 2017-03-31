package Project.Handler.Information;

import Project.Model.Person.Parent;
import Project.Model.Person.Person;
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

    public ArrayList<Parent> getParentsByStudentId(int personId) {
        ArrayList<Parent> parents = parentPersistent.getParentsByStudentId(personId);
        for (Parent it : parents) {
            it.setAddresses(personPersistent.getPersonAddressesByPersonId(it.getId()));
        }
        return parents;
    }

    public boolean addParentAndStudentRelationships(int personPId, ArrayList<Integer> personSIds){
        boolean result = true;
        for(int it: personSIds) {
            if(!parentPersistent.addParentAndStudentRelationship(personPId,it)){
                result = false;
            }
        }
        return result;
    }

    public ArrayList<Person> getAllParents(){
        return parentPersistent.getAllParents();
    }

    public boolean clearFamily(int personId){
        return parentPersistent.clearFamily(personId);
    }

    public boolean clearParent(int personId){
        return parentPersistent.clearParent(personId);
    }
}
