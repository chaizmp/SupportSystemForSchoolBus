package Project.Persistent.SQL;

import Project.Mapper.ParentMapper;
import Project.Model.Person.Parent;
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
public class ParentPersistent extends JdbcTemplate{

    @Autowired
    public ParentPersistent(DataSource mainDataSource){
        super();
        this.setDataSource(mainDataSource);
    }

    public ArrayList<Parent> getParentsByStudentId(String personId)
    {
        List<Parent> parentList =  query("SELECT * FROM Person,Parent WHERE Person.personId = Parent.personId and Person.personId in (" +
                "SELECT personPId from Family WHERE personSID = ? )",new ParentMapper(),personId);
        return new ArrayList<>(parentList);
    }
}
