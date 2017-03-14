package Project.Persistent.SQL;

import Project.Mapper.AddressMapper;
import Project.Mapper.PersonMapper;
import Project.Model.Enumerator.Role;
import Project.Model.Person.Person;
import Project.Model.Position.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    public ArrayList<Address> getPersonAddressesByPersonId(int personId) {
        List<Address> addressList = query("SELECT * FROM PersonAddress,AddressDetail WHERE PersonAddress.latitude = AddressDetail.latitude " +
                "and PersonAddress.longitude = AddressDetail.longitude and PersonAddress.personId = ?", new AddressMapper(), personId);
        return new ArrayList<>(addressList);
    }

    public Boolean updateFireBaseToken(int personId, String token) {
        try {
            return update("UPDATE person SET token = ? WHERE personId = ?", token, personId) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPersonToken(int personId) {
        return queryForObject("SELECT token From Person where personId = ?", String.class, personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(int personId) {
        List<Person> teacherList = query("SELECT * from person, teachHistory WHERE person.personId = teachHistory.personTid " +
                "AND personSId = ? AND year = ?", new PersonMapper(), personId, "" + Calendar.getInstance().get(Calendar.YEAR));
        List<Person> parentList = query("SELECT * from person, family WHERE person.personId = family.personPid AND personSid = ?", new PersonMapper(), personId);
        List<Person> personList = new ArrayList<>();
        personList.addAll(teacherList);
        personList.addAll(parentList);
        return new ArrayList<>(personList);
    }

    public Person getPersonByPersonId(int personId){
        try {
            return queryForObject("SELECT * from Person WHERE personId = ?", new PersonMapper(), personId);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int addPerson(String username, String hash, String salt, String name, String surname, Role role, String tel){
        try{
            update("INSERT INTO person(username, password, salt, name, surname, role,  tel) VALUES(?, ?, ?, ?, ? ,?, ?)", username, hash, salt, name, surname, role.name(), tel);
            return queryForObject("SELECT last_insert_id()",Integer.class);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public String getSalt(String username){
        try{
            return queryForObject("SELECT salt FROM person WHERE username = ?", String.class, username);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getPassword(String username){
        try{
            return queryForObject("SELECT password FROM person WHERE username = ?", String.class, username);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPersonAddressByPersonId(int personId, double latitude, double longitude){
        try{
            return update("INSERT INTO personAddress(personId, latitude, longitude) VALUES(?, ?, ?)", personId, latitude, longitude) == 1;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAddressDetail(double latitude, double longitude, String detail){
        try{
            return update("INSERT INTO addressDetail(latitude, longitude, detail) VALUES(?, ?, ?)", latitude, longitude, detail) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setAlarm(int personId, int duration){
        try{
            return update("UPDATE person SET alarm = ? WHERE personId = ?", duration, personId) == 1;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int getPersonAlarm(int personId){
        return queryForObject("SELECT alarm FROM person WHERE personId = ?", Integer.class, personId);
    }

    public String getRoleByUsername(String username){
        return queryForObject("SELECT role FROM person WHERE username = ?", String.class, username);
    }

    public int getPersonIdByUsername(String username){
        return queryForObject("SELECT personId from person WHERE username = ?", Integer.class, username);
    }
}
