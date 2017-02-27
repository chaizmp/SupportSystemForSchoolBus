package Project.Persistent.SQL;

import Project.Mapper.SchoolMapper;
import Project.Model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by User on 27/2/2560.
 */
@Service
public class SchoolPersistent extends JdbcTemplate {

    @Autowired
    public SchoolPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }


    public School getSchoolDetail(String schoolName){
        try {
            return queryForObject("SELECT * FROM School WHERE name = ?", new SchoolMapper(), schoolName);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
