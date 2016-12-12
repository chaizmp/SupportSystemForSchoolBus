package Project.Mapper;

import Project.Model.Driver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 2/9/2559.
 */
public class DriverMapper implements RowMapper<Driver>{

    @Override
    public Driver mapRow(ResultSet rs,int rowNum) throws SQLException
    {
        return new Driver(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));

    }
}
