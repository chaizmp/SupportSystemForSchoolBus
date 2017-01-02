package Project.Persistent.SQL;

import Project.Model.Position.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class BusPersistent extends JdbcTemplate{

    @Autowired
    public BusPersistent(DataSource mainDataSource){
        super();
        this.setDataSource(mainDataSource);
    }

    public Bus getCurrentBusByStudentId(String personId){
        return queryForObject("SELECT * FROM PersonInBus, ")
    }
}
