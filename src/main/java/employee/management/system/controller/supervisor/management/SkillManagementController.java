package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.*;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.EmployeeSkillService;
import employee.management.system.service.PositionService;
import employee.management.system.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class SkillManagementController {

    private final PositionService positionService;
    private final SkillService skillService;
    private final EmployeeService employeeService;
    private final EmployeeSkillService employeeSkillService;
    public SkillManagementController(PositionService positionService, SkillService skillService, EmployeeService employeeService, EmployeeSkillService employeeSkillService) {
        this.positionService = positionService;
        this.skillService = skillService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
    }

    @GetMapping("/add-skill")
    public String showSkillForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Skill skill = new Skill();
        model.addAttribute("skill", skill);

        List<Position> positions = positionService.getPositionForAddForm();
        model.addAttribute("positions", positions);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/skills/form";
    }

    @PostMapping("/add-skill")
    public String addSkill(@ModelAttribute("skill") Skill skill,
                           RedirectAttributes redirectAttributes) {

        boolean skillExist = skillService.existsBySkillName(skill.getSkillName());
        if(skillExist){
            redirectAttributes.addFlashAttribute("errorMessage", "Umiejętność o podanej nazwie już istnieje");
            return "redirect:/supervisor-panel/management/add-skill";
        }

        skillService.addSkill(skill);

        List<Position> selectedPositions = new ArrayList<>();
        List<Integer> positionIds = new ArrayList<>();
        for (Position position : skill.getPositions()) {
            positionIds.add(position.getId());
        }

        for (Integer id:positionIds){
            Position nextPosition = positionService.findPositionById(id);
            selectedPositions.add(nextPosition);
            nextPosition.setSkill(skill);
            positionService.updatePosition(nextPosition);

        }
        skill.setPositions(selectedPositions);


        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee:employees) {
            EmployeeSkill employeeSkill = new EmployeeSkill(employee, skill);
            employeeSkillService.addEmployeeSkill(employeeSkill);
        }


        return "redirect:/supervisor-panel/management";
    }

}
