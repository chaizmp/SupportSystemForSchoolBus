package Project.Handler.Information;

import Project.Model.Position.Bus;
import Project.Persistent.SQL.BusPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class BusHandler {

    @Autowired
    BusPersistent busPersistent;

    public String getBusDetailByStudentId(String personId)
    {
        Bus bus = busPersistent.getCurrentBusByStudentId(personId);

    }
}

