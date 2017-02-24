package Project.PersistentLayer;

import Project.Mapper.ParentMapper;
import Project.Model.Person.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by User on 30/8/2559.
 */
@Service
public class ParentPersistent extends JdbcTemplate {
    @Autowired
    public ParentPersistent(DataSource mainDataSource) {
        super();
        this.setDataSource(mainDataSource);
    }

    public void addParent(String id, String name, String surName, String telNum, String address) {
        update("INSERT INTO student(`id`, `name`,'surName','telNum','address') VALUES (?,?,?,?,?)", new Object[]{id, name, surName, telNum, address}); // not yet completed
    }

    public boolean parentIsExist(String id) {
        if (queryForObject("SELECT count(*) from Parent WHERE parentId = ?", Integer.class, id) == 1) {
            return true;
        }
        return false;
    }

    public Parent getParentByStudentId(String id) {
        List<Parent> parList = query("SELECT * FROM PARENT WHERE parentId = (SELECT parentId FROM FAMILY  WHERE studentId =  '" + id + "')", new ParentMapper());
        Parent parent = null;
        for (Parent it : parList) {
            if (it != null) {
                parent = it;
            }
        }
        return parent;
    }
}
