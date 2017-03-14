package Project.Mapper;

import Project.Model.Enumerator.Role;
import Project.Model.Person.Driver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 3/1/2560.
 */
public class DriverMapper implements RowMapper<Driver> {

    @Override
    public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Driver(Role.valueOf(rs.getString("role")), rs.getInt("personId"), null, rs.getString("token"), rs.getString("tel"),
                rs.getString("username"), rs.getString("name"), rs.getString("surName"), rs.getString("facebookId"), null);
    }

}