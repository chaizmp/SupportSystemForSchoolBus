package Project.Mapper;

import Project.Model.Position.Bus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 2/9/2559.
 */
public class BusMapper implements RowMapper<Bus> {
    @Override
    public Bus mapRow(ResultSet rs,int rowNum) throws SQLException
    {
        return new Bus(rs.getString(1));
    }

}