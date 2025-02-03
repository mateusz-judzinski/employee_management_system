package employee.management.system.controller.supervisor.management;

import employee.management.system.dto.NewEmployeeDTO;
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
public class EmployeeManagementController {

    private final SkillService skillService;
    private final EmployeeService employeeService;
    private final EmployeeSkillService employeeSkillService;
    private final QualificationService qualificationService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final PositionEmployeeHistoryService historyService;

    public EmployeeManagementController(SkillService skillService, EmployeeService employeeService, EmployeeSkillService employeeSkillService, QualificationService qualificationService, PasswordEncoder passwordEncoder, UserService userService, PositionEmployeeHistoryService historyService) {
        this.skillService = skillService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
        this.qualificationService = qualificationService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.historyService = historyService;
    }

    @GetMapping("/add-employee")
    public String showAddEmployeeForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Employee employee = new Employee();
        List<EmployeeSkill> skills = new ArrayList<>();

        List<Skill> tempSkills = skillService.findAllSkills();
        for (Skill skill:tempSkills){
            EmployeeSkill nextSkill = new EmployeeSkill();
            nextSkill.setSkill(skill);
            nextSkill.setProficiencyLevel(0);
            skills.add(nextSkill);
        }

        List<Qualification> qualifications = qualificationService.findAllQualifications();
        qualifications.removeIf(q -> q.getName().equals("stała przepustka") || q.getName().equals("brak stałej przepustki"));


        NewEmployeeDTO newEmployee = new NewEmployeeDTO(employee, skills, qualifications);
        model.addAttribute("newEmployee", newEmployee);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/employees/form";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute @Valid NewEmployeeDTO newEmployee,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/add-employee";
        }

        if(newEmployee.getEmployee().getIdCardNumber() != null){
            boolean idCardExists = employeeService.existsByIdCardNumber(newEmployee.getEmployee().getIdCardNumber());
            if(idCardExists){
                redirectAttributes.addFlashAttribute("errorMessage", "Stała przepustka o podanym numerze już istnieje");
                return "redirect:/supervisor-panel/management/add-employee";
            }
        }

        if(newEmployee.getEmployee().getIdCardNumber() == null){
            Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
            newEmployee.getQualifications().add(noIdCardQualification);
        } else{
            Qualification idCardQualification = qualificationService.findQualificationByName("stała przepustka");
            newEmployee.getQualifications().add(idCardQualification);
        }

        newEmployee.getEmployee().setQualifications(newEmployee.getQualifications());
        Employee savedEmployee = employeeService.addEmployee(newEmployee.getEmployee());

        for (EmployeeSkill skill : newEmployee.getSkills()) {
            skill.setEmployee(savedEmployee);
            skill.setTimeExperienceByProficiencyLevel(skill.getProficiencyLevel());
        }

        employeeSkillService.saveAll(newEmployee.getSkills());

        return "redirect:/supervisor-panel/management";
    }

    @GetMapping("/edit-employee/{id}")
    public String showEditEmployeeForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes,
                                       @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Employee employee = employeeService.findEmployeeById(id);

        if(employee == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono pracownika o ID: " + id);
            return "redirect:/error-handler";
        }

        model.addAttribute("employee", employee);

        List<Qualification> qualifications = qualificationService.getQualificationsForManagement();
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/employees/edit";
    }

    @PostMapping("/edit-employee")
    public String updateEmployee(@ModelAttribute @Valid Employee employee,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("id", employee.getId());
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/edit-employee/{id}";
        }

        if(employee.getIdCardNumber() != null){
            boolean isIdCardOccupied = employeeService.isIdCardOccupied(employee);
            if(isIdCardOccupied){
                redirectAttributes.addAttribute("id", employee.getId());
                redirectAttributes.addFlashAttribute("errorMessage", "Stała przepustka o podanym numerze już istnieje");
                return "redirect:/supervisor-panel/management/edit-employee/{id}";
            }
        }

        Employee employeeBeforeUpdate = employeeService.findEmployeeById(employee.getId());

        if(employeeBeforeUpdate == null){
            redirectAttributes.addAttribute("id", employee.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono pracownika o ID: " + employee.getId());
            return "redirect:/supervisor-panel/management/edit-employee/{id}";
        }

        List<Qualification> qualifications = new ArrayList<>();
        List<Integer> qualificationsIds = new ArrayList<>();
        for (Qualification qualification:employee.getQualifications()){
            qualificationsIds.add(qualification.getId());
        }
        for (Integer id:qualificationsIds) {
            Qualification nextQualification = qualificationService.findQualificationById(id);
            qualifications.add(nextQualification);
        }

        Qualification hasIdCard = qualificationService.findQualificationByName("stała przepustka");
        Qualification notHaveIdCard = qualificationService.findQualificationByName("brak stałej przepustki");

        if(employee.getIdCardNumber() == null){
            qualifications.add(notHaveIdCard);
        }
        else{
            qualifications.add(hasIdCard);
        }

        employee.setQualifications(qualifications);

        employee.setPosition(employeeBeforeUpdate.getPosition());
        employee.setSkills(employeeBeforeUpdate.getSkills());
        employee.setShifts(employeeBeforeUpdate.getShifts());

        employeeService.updateEmployee(employee);

        return "redirect:/supervisor-panel/management";
    }


    @PostMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") int id,
                                      @RequestParam("password") String password,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User currentUser = userService.findUserByUsername(username);

        if (currentUser == null || !passwordEncoder.matches(password, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podane hasło jest nieprawidłowe. Usunięcie nie zostało wykonane.");
            return "redirect:/supervisor-panel/management";
        }

        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono pracownika.");
            return "redirect:/supervisor-panel/management";
        }

        List<PositionEmployeeHistory> historyToRemove = historyService.findByEmployeeId(employee.getId());
        for(PositionEmployeeHistory history:historyToRemove){
            historyService.deleteHistoryById(history.getId());
        }

        employeeService.deleteEmployeeById(id);
        return "redirect:/supervisor-panel/management";
    }

}
