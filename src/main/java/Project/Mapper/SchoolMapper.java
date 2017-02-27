package Project.Mapper;

import Project.Model.School;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 27/2/2560.
 */
public class SchoolMapper implements RowMapper<School> {
    public School mapRow(ResultSet rs, int rowNum) throws SQLException{
        return new School(rs.getString("name"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("addressDetail"));
    }
}
