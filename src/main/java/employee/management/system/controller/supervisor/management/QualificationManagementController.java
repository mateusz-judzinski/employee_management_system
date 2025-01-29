package employee.management.system.controller.supervisor.management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor-panel/management")
public class QualificationManagementController {

    @GetMapping("/add-qualification")
    public String showQualificationForm(){

        return "supervisor/qualifications/form";
    }
}
