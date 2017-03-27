package Project.Persistent.SQL;

import Project.Mapper.ParentMapper;
import Project.Mapper.PersonMapper;
import Project.Model.Person.Parent;
import Project.Model.Person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class ParentPersistent extends JdbcTemplate {

    @Autowired
    public ParentPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Parent> getParentsByStudentId(int personId) {
        List<Parent> parentList = query("SELECT * FROM Person,Parent WHERE Person.personId = Parent.personId and Person.personId in (" +
                "SELECT personPId from Family WHERE personSID = ? )", new ParentMapper(), personId);
        return new ArrayList<>(parentList);
    }

    public boolean addParent(int personId){
        try{
            return update("INSERT INTO parent(personId) VALUE(?)", personId) == 1;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public boolean addParentAndStudentRelationship(int personPId, int personSId){
        try{
            return update("INSERT INTO family(personPId, personSid) VALUES(?, ?)", personPId, personSId) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Person> getAllParents(){
        try{
            return new ArrayList<>(query("SELECT * FROM Person WHERE Role = 'PARENT'", new PersonMapper()));
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
