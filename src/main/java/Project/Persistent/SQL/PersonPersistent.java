package Project.Persistent.SQL;

import Project.Mapper.AddressMapper;
import Project.Model.Person.Person;
import Project.Model.Position.Address;
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
public class PersonPersistent extends JdbcTemplate {
    @Autowired
    public PersonPersistent(DataSource mainDataSource)
    {
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Address> getPersonAddressesByPersonId(String personId){
        List<Address> addressList =  query("SELECT * FROM PersonAddress,AddressDetail WHERE PersonAddress.latitude = AddressDetail.latitude " +
                "and PersonAddress.longitude = AddressDetail.longitude and PersonAddress.personId ="+personId,new AddressMapper());
        return new ArrayList<>(addressList);
    }
}
