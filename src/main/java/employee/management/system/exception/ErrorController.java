package employee.management.system.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String showAccessDeniedPage(){
        return "user/access-denied";
    }
}
