package Project.Handler.Information;

import Project.Handler.Position.PositionHandler;
import Project.Model.Enumerator.Role;
import Project.Model.Person.Person;
import Project.Persistent.SQL.PersonPersistent;
import Project.Persistent.SQL.StudentPersistent;
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
    @Autowired
    StudentPersistent studentPersistent;
    @Autowired
    PositionHandler positionHandler;
    @Autowired
    StudentHandler studentHandler;
    @Autowired
    ParentHandler parentHandler;

    public Boolean updateFireBaseToken(int personId, String token) {
        return personPersistent.updateFireBaseToken(personId, token);
    }

    public String getPersonToken(int personId) {
        return personPersistent.getPersonToken(personId);
    }

    public ArrayList<Person> getPersonsRelatedToStudent(int personId) {
        return personPersistent.getPersonsRelatedToStudent(personId);

    }

    public boolean addPersonAddressById(int personId, ArrayList<String> details) {
        boolean result = true;
        for (int i = 0; i < details.size(); i++) {
            if (!personPersistent.addAddressDetail(personId, details.get(i))) {
                result = false;
            }
        }
        return result;
    }

    public Person getPersonByPersonId(int personId) {
        return personPersistent.getPersonByPersonId(personId);
    }

    public boolean setAlarm(int personId, int duration) {
        return personPersistent.setAlarm(personId, duration);
    }

    public int getTeacherAlarm(int personTId, int personSId) {
        return personPersistent.getTeacherAlarm(personTId, personSId);
    }

    public int getParentAlarm(int personPId, int personSId) {
        return personPersistent.getParentAlarm(personPId, personSId);
    }

    public String getRoleByUsername(String username) {
        return personPersistent.getRoleByUsername(username);
    }

    public int getPersonIdByUsername(String username) {
        return personPersistent.getPersonIdByUsername(username);
    }

    public boolean savePersonImage(String image, int personId) {
        try {
            String path = new File(".").getCanonicalPath() + "\\userData\\" + personId + "\\img\\face.jpg";
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(Base64.getDecoder().decode(image));
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPersonImage(int personId) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public ArrayList<Person> getAllPersonsExceptStudents() {
        ArrayList<Person> personList = personPersistent.getAllPersonsExceptStudents();
        return personList;
    }

    public boolean deletePersonByPersonId(int personId) {
        Role role = studentPersistent.getRoleByPersonId(personId);
        if (!positionHandler.everInBus(personId)) {
            personPersistent.deleteAddresses(personId);
            positionHandler.deleteFromRoute(personId);
            switch (role) {
                case TEACHER:
                    teacherHandler.clearTeachHistory(personId);
                    teacherHandler.clearTeacher(personId);
                    break;
                case DRIVER:
                    break;
                case STUDENT:
                    studentHandler.clearFamily(personId);
                    studentHandler.clearTeachHistory(personId);
                    studentHandler.clearStudent(personId);
                    break;
                case PARENT:
                    parentHandler.clearFamily(personId);
                    parentHandler.clearParent(personId);
                    break;
                case SCHOOLOFFICER:
                    break;
                default:
                    break;
            }
            personPersistent.deletePerson(personId);
        }
            return false;
    }
}
