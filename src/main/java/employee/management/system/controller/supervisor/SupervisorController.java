package employee.management.system.controller.supervisor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor-panel")
public class SupervisorController {

    @GetMapping()
    public String getSupervisorPanel() {
        return "supervisor/panel";
    }
}
