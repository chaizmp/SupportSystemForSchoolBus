package Project.Mapper;

import Project.Model.Position.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 2/1/2560.
 */
public class AddressMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Address(rs.getString("detail"), rs.getDouble("latitude"), rs.getDouble("longitude"));
    }

}
