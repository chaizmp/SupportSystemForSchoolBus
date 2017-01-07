package Project.Mapper;

import Project.Model.Position.Bus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 2/1/2560.
 */
public class BusMapper implements RowMapper<Bus> {

    @Override
    public Bus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Bus(rs.getString("carNumber"));
    }


}
