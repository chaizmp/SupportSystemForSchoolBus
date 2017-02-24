package Project.Persistent.SQL;

import Project.Mapper.AddressMapper;
import Project.Mapper.PersonMapper;
import Project.Model.Person.Person;
import Project.Model.Position.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class PersonPersistent extends JdbcTemplate {
    @Autowired
    public PersonPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Address> getPersonAddressesByPersonId(String personId) {
        List<Address> addressList = query("SELECT * FROM PersonAddress,AddressDetail WHERE PersonAddress.latitude = AddressDetail.latitude " +
                "and PersonAddress.longitude = AddressDetail.longitude and PersonAddress.personId = ?", new AddressMapper(), personId);
        return new ArrayList<>(addressList);
    }

    public Boolean updateFireBaseToken(String personId, String token) {
        try {
            return update("UPDATE person SET token = ? WHERE personId = ?", token, personId) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPersonToken(String personId) {
        return queryForObject("SELECT token From Person where personId = ?", String.class, personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(String personId){
        List<Person> teacherList = query("SELECT * from person, teachHistory WHERE person.personId = teachHistory.personTid " +
                "AND personSId = ? AND year = ?", new PersonMapper(), personId, ""+ Calendar.getInstance().get(Calendar.YEAR) );
        List<Person> parentList = query("SELECT * from person, family WHERE person.personId = family.personPid AND personSid = ?", new PersonMapper(), personId);
        List<Person> personList = new ArrayList<>();
        personList.addAll(teacherList);
        personList.addAll(parentList);
        return new ArrayList<>(personList);
    }

}
