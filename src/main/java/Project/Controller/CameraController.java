package Project.Controller;

import Project.Handler.ApiCalls.ApiCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * Created by User on 11/3/2560.
 */
@RestController
public class CameraController {
    @Autowired
    ApiCall apiCall;
    @RequestMapping(value = "getPicFromCamera", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean getPicFromCamera(
    ) {
      //return apiCall.getPicFromCamera();
        try {
            URL url = new URL("http://192.168.1.11:81/snapshot.cgi?user=admin&pwd=sakchailab606");
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream("C:\\borrowed_image.jpeg");
            fos.write(response);
            fos.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

}
