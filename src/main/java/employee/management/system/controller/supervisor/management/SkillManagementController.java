package employee.management.system.controller.supervisor.management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor-panel/management")
public class SkillManagementController {

    @GetMapping("/add-skill")
    public String showSkillForm(){

        return "supervisor/skills/form";
    }

}
