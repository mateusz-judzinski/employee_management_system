package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Position;
import employee.management.system.entity.Skill;
import employee.management.system.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class PositionManagementController {

    private final SkillService skillService;

    public PositionManagementController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/add-position")
    public String showPositionForm(Model model){

        Position position = new Position();
        model.addAttribute("position", position);

        List<Skill> skill = skillService.findAllSkills();
        model.addAttribute("skills", skill);

        return "supervisor/positions/form";
    }


}
