package Project.Handler.Information;

import Project.Model.Enumerator.TypeOfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by User on 1/1/2560.
 */
@Service
public class StudentHandler extends JdbcTemplate{

    @Autowired
    public StudentHandler(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public boolean setTypeOfService(TypeOfService typeOfService,String personSId)
    {
        boolean result = false;
        try {
            update("UPDATE `STUDENT` SET `typeOfService`= ? WHERE `personId` = ?",typeOfService.name(),personSId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
