package Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by User on 29/8/2559.
 */
@RestController
public class MemberController {

    @RequestMapping(value = "signOnWithFb" , method = RequestMethod.POST)
    public @ResponseBody // return type and method body not yet finished
    String signOnWithFacebook
    (
            @RequestParam(value = "facebookId") String faceBookId
    )
    {
        return new String("hehe");
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody // return type and param list and body not yet finished
    String register
    (
            @RequestParam(value = "userName", required = false) String userName
    )
    {
        return new String("hehehe");
    }


}
