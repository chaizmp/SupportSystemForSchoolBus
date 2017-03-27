package Project.Handler.Information;

import Project.Model.Enumerator.Role;
import Project.Model.Person.Person;
import Project.Persistent.SQL.PersonPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by User on 20/2/2560.
 */
@Service
public class PersonHandler {
    @Autowired
    PersonPersistent personPersistent;
    @Autowired
    TeacherHandler teacherHandler;

    public Boolean updateFireBaseToken(int personId, String token) {
        return personPersistent.updateFireBaseToken(personId, token);
    }

    public String getPersonToken(int personId) {
        return personPersistent.getPersonToken(personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(int personId) {
        return personPersistent.getPersonsRelatedToStudent(personId);

    }

    public boolean addPersonAddressById(int personId, ArrayList<Double> latitudes, ArrayList<Double> longitudes, ArrayList<String> details){
        boolean result = true;
        for(int i = 0; i< latitudes.size(); i++){
            if(!personPersistent.addPersonAddressByPersonId(personId, latitudes.get(i), longitudes.get(i))) {
                result = false;
            }
            if(!personPersistent.addAddressDetail(latitudes.get(i),longitudes.get(i), details.get(i))){
                result = false;
            }
        }
        return result;
    }

    public Person getPersonByPersonId(int personId){
        return personPersistent.getPersonByPersonId(personId);
    }

    public boolean setAlarm(int personId, int duration){
        return personPersistent.setAlarm(personId, duration);
    }

    public int getPersonAlarm(int personId){
        return personPersistent.getPersonAlarm(personId);
    }

    public String getRoleByUsername(String username){
       return personPersistent.getRoleByUsername(username);
    }
    public int getPersonIdByUsername(String username){
        return personPersistent.getPersonIdByUsername(username);
    }
    public boolean savePersonImage(String image, int personId){
        try {
            String path = new File(".").getCanonicalPath()+"\\userData\\"+personId+"\\img\\face.jpg";
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(Base64.getDecoder().decode(image));
            fos.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public String getPersonImage(int personId){
        try {
            String path = new File(".").getCanonicalPath() + "\\userData\\" + personId + "\\img\\face.jpg";
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
