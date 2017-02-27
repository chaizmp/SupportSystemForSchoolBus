package Project.Handler.Information;

import Project.Model.School;
import Project.Persistent.SQL.SchoolPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by User on 27/2/2560.
 */
@Service
public class SchoolHandler {
    @Autowired
    SchoolPersistent schoolPersistent;

    public School getSchoolDetail(String schoolName){
        return schoolPersistent.getSchoolDetail(schoolName);
    }
}
