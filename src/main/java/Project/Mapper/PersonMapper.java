package Project.Mapper;

import Project.Model.Enumerator.Role;
import Project.Model.Person.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 24/2/2560.
 */
public class PersonMapper implements RowMapper<Person> {
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Person(Role.valueOf(rs.getString("role")), rs.getInt("personId"), null, rs.getString("token"), rs.getString("tel"), rs.getString("username"), rs.getString("name"), rs.getString("surName"), rs.getString("facebookId"), null);
    }
}
