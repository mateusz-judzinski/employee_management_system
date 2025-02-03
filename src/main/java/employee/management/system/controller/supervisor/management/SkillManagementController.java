package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.*;
import employee.management.system.service.*;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class SkillManagementController {

    private final PositionService positionService;
    private final SkillService skillService;
    private final EmployeeService employeeService;
    private final EmployeeSkillService employeeSkillService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SkillManagementController(PositionService positionService, SkillService skillService, EmployeeService employeeService, EmployeeSkillService employeeSkillService, UserService userService, PasswordEncoder passwordEncoder) {
        this.positionService = positionService;
        this.skillService = skillService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/add-skill")
    public String showSkillForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {

        Skill skill = new Skill();
        model.addAttribute("skill", skill);

        List<Position> positions = positionService.getPositionForAddForm();
        model.addAttribute("positions", positions);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/skills/form";
    }

    @PostMapping("/add-skill")
    public String addSkill(@ModelAttribute @Valid Skill skill,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/add-skill";
        }

        boolean skillExist = skillService.existsBySkillName(skill.getSkillName());
        if (skillExist) {
            redirectAttributes.addFlashAttribute("errorMessage", "Umiejętność o podanej nazwie już istnieje");
            return "redirect:/supervisor-panel/management/add-skill";
        }

        skillService.addSkill(skill);

        List<Position> selectedPositions = new ArrayList<>();
        List<Integer> positionIds = new ArrayList<>();
        for (Position position : skill.getPositions()) {
            positionIds.add(position.getId());
        }

        for (Integer id : positionIds) {
            Position nextPosition = positionService.findPositionById(id);
            selectedPositions.add(nextPosition);
            nextPosition.setSkill(skill);
            positionService.updatePosition(nextPosition);

        }
        skill.setPositions(selectedPositions);


        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            EmployeeSkill employeeSkill = new EmployeeSkill(employee, skill);
            employeeSkillService.addEmployeeSkill(employeeSkill);
        }


        return "redirect:/supervisor-panel/management";
    }

    @GetMapping("/edit-skill/{id}")
    public String showSkillEditForm(@PathVariable("id") int id, Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage, RedirectAttributes redirectAttributes) {

        Skill skill = skillService.findSkillById(id);

        if (skill == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono umiejętności o ID: " + id);
            return "redirect:/error-handler";
        }

        model.addAttribute("skill", skill);

        List<Position> positions = positionService.getPositionForEditForm(id);
        model.addAttribute("positions", positions);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/skills/edit";
    }


    @PostMapping("/edit-skill")
    public String updateSkill(@ModelAttribute @Valid Skill skill,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("id", skill.getId());
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/edit-skill/{id}";
        }

        Skill skillBeforeUpdate = skillService.findSkillById(skill.getId());

        if (skillBeforeUpdate == null) {
            redirectAttributes.addAttribute("id", skill.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono umiejętności o ID: " + skill.getId());
            return "redirect:/supervisor-panel/management/edit-skill/{id}";
        }

        boolean isSkillNameOccupied = skillService.isSkillNameOccupied(skill);
        if (isSkillNameOccupied) {
            redirectAttributes.addAttribute("id", skill.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Umiejętność o podanej nazwie już istnieje.");
            return "redirect:/supervisor-panel/management/edit-skill/{id}";
        }

        skill.setEmployees(skillBeforeUpdate.getEmployees());

        List<Position> previousPositions = positionService.findPositionBySkillId(skill.getId());
        if (skill.getPositions() != null) {
            List<Position> positions = new ArrayList<>();
            List<Integer> positionIds = new ArrayList<>();
            for (Position position : skill.getPositions()) {
                positionIds.add(position.getId());
            }
            for (Integer id : positionIds) {
                Position nextPosition = positionService.findPositionById(id);
                previousPositions.remove(nextPosition);
                positions.add(nextPosition);
                nextPosition.setSkill(skill);
            }
            skill.setPositions(positions);
        }

        else {
            skill.setPositions(null);
        }

        if (!previousPositions.isEmpty()) {
            for (Position previousPosition : previousPositions) {
                previousPosition.setSkill(null);
            }
        }

        skillService.updateSkill(skill);

        return "redirect:/supervisor-panel/management";
    }

    @PostMapping("/delete-skill/{id}")
    public String deleteSkill(@PathVariable("id") int id,
                              @RequestParam("password") String password,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User currentUser = userService.findUserByUsername(username);

        if (currentUser == null || !passwordEncoder.matches(password, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podane hasło jest nieprawidłowe. Usunięcie nie zostało wykonane.");
            return "redirect:/supervisor-panel/management";
        }

        Skill skill = skillService.findSkillById(id);
        if (skill == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono umiejętności.");
            return "redirect:/supervisor-panel/management";
        }

        if(!skill.getPositions().isEmpty()){
            for(Position position:skill.getPositions()){
                position.setSkill(null);
            }
        }

        List<EmployeeSkill> employeeSkillsToRemove = employeeSkillService.findAllEmployeeSkillBySkillId(skill.getId());
        for(EmployeeSkill employeeSkill:employeeSkillsToRemove){
            employeeSkillService.deleteById(employeeSkill.getId());
        }

        skillService.deleteSkillById(id);
        return "redirect:/supervisor-panel/management";
    }

}