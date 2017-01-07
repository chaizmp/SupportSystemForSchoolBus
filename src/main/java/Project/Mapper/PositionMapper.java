package Project.Mapper;

import Project.Model.Enumerator.Status;
import Project.Model.Position.Position;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 6/1/2560.
 */
public class PositionMapper implements RowMapper<Position> {
    public Position mapRow(ResultSet rs, int rowNum) throws SQLException{
        return new Position(rs.getFloat("latitude"),rs.getFloat("longitude"), Status.valueOf(rs.getString("status")),rs.getTimestamp("atTime"));
    }
}
