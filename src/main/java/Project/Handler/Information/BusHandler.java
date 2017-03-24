package Project.Handler.Information;

import Project.Handler.Position.PositionHandler;
import Project.Model.Position.Bus;
import Project.Model.Position.Route;
import Project.Persistent.SQL.BusPersistent;
import Project.Persistent.SQL.StudentPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by User on 2/1/2560.
 */
@Service
public class BusHandler {

    @Autowired
    BusPersistent busPersistent;

    @Autowired
    StudentPersistent studentPersistent;

    @Autowired
    PositionHandler positionHandler;

    public Bus getCurrentBusCarIdByStudentId(int personId) {
        return busPersistent.getCurrentBusCarIdByStudentId(personId);
    }

    public Bus getLatestBusCarIdByStudentId(int personId) {
        return busPersistent.getLatestBusCarIdByStudentId(personId);
    }

    // A solution to get the bus trip that a student takes is
    // to make the bus log period subset of bus position
    // when the first person gets on, a record which has status 'START' will be inserted into the bus position table too
    // and when the last person get off, a record which has status 'FINISH' will be inserted also
    public Bus getBusCarIdByStudentIdAndAtTime(int personId, Timestamp atTime) {
        return busPersistent.getBusCarIdByStudentIdAndAtTime(personId, atTime);
    }

    public Bus getCurrentBusPosition(int carId) {
        return busPersistent.getCurrentBusPosition(carId);
    }

    public boolean setVelocityToZero(int carId) {
        return busPersistent.setVelocityToZero(carId);
    }

    public ArrayList<Bus> getAllBus() {
        return busPersistent.getAllBus();
    }

    public boolean setVelocityAndCheckPointPassed(int carId, double averageVelocity, int checkPointPassed){
        return busPersistent.setVelocityAndCheckPointPassed(carId, averageVelocity, checkPointPassed);
    }
    public double getAverageVelocity(int carId){
        return busPersistent.getAverageVelocity(carId);
    }
    public int getCheckPointPassed(int carId){
        return busPersistent.getCheckPointPassed(carId);
    }
    public Route getBusRoutinelyUsedRoute(int carId){
        return positionHandler.getBusRoutinelyUsedRoute(carId);
    }
    public String getBusCarNumberByCarId(int carId){
        return busPersistent.getBusCarNumberByCarId(carId);
    }

    public boolean saveCameraImage(int carId, byte[] imageFront, boolean isFront){
        try {
            String filename = isFront? "front.jpeg":"back.jpeg";
            String path = new File(".").getCanonicalPath()+"\\busData\\"+carId+"\\img\\"+filename;
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(imageFront);
            fos.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getImageFromBus(int carId, boolean isFront){
        String filename = isFront? "front.jpeg":"back.jpeg";
        try {
            String path = new File(".").getCanonicalPath() + "\\busData\\" + carId + "\\img\\" + filename;
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            return Base64.getEncoder().encodeToString(response);
        }catch(Exception e){
            e.printStackTrace();
            return "-1";
        }

    }
}

