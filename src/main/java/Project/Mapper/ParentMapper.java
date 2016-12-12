package Project.Mapper;

import Project.Model.Parent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 5/9/2559.
 */
public class ParentMapper implements RowMapper<Parent> {
    @Override
    public Parent mapRow(ResultSet rs,int rowNum) throws SQLException
    {
        return new Parent(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
    }
}
