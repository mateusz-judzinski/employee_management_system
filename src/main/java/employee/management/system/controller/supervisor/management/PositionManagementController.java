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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/supervisor-panel/management")
public class PositionManagementController {

    private final SkillService skillService;
    private final QualificationService qualificationService;
    private final PositionService positionService;
    private final PositionEmployeeHistoryService historyService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PositionManagementController(SkillService skillService, QualificationService qualificationService, PositionService positionService, PositionEmployeeHistoryService historyService, EmployeeService employeeService, UserService userService, PasswordEncoder passwordEncoder) {
        this.skillService = skillService;
        this.qualificationService = qualificationService;
        this.positionService = positionService;
        this.historyService = historyService;
        this.employeeService = employeeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/add-position")
    public String showPositionForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Position position = new Position();
        model.addAttribute("position", position);

        List<Skill> skill = skillService.findAllSkills();
        model.addAttribute("skills", skill);

        List<Qualification> qualifications = qualificationService.findAllQualifications();
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/positions/form";
    }

    @PostMapping("/add-position")
    public String addPosition(@ModelAttribute @Valid Position position,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/add-position";
        }

        boolean positionExists = positionService.existsByPositionName(position.getPositionName());

        if (positionExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stanowisko o podanej nazwie już istnieje");
            return "redirect:/supervisor-panel/management/add-position";
        }

        if (position.getSkill() != null) {
            Skill skill = skillService.findSkillById(position.getSkill().getId());
            position.setSkill(skill);
        }

        List<Qualification> selectedQualifications = new ArrayList<>();
        List<Integer> qualificationIds = new ArrayList<>();
        for (Qualification qualification : position.getNeededQualifications()) {
            qualificationIds.add(qualification.getId());
        }

        Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
        Qualification idCardQualification = qualificationService.findQualificationByName("stała przepustka");

        int count = 0;
        for (Integer id : qualificationIds) {
            Qualification nextQualification = qualificationService.findQualificationById(id);
            if(Objects.equals(nextQualification.getName(), noIdCardQualification.getName()) || Objects.equals(nextQualification.getName(), idCardQualification.getName())){
                count++;
            }
            selectedQualifications.add(nextQualification);
        }

        if(count == 2){
            redirectAttributes.addFlashAttribute("errorMessage", "Nie można jednocześnie wybrać opcji 'stała przepustka' i 'brak stałej przepustki'");
            return "redirect:/supervisor-panel/management/add-position";
        }

        position.setNeededQualifications(selectedQualifications);

        position.setIsActive(false);
        position.setTemporary(false);

        positionService.addPosition(position);

        return "redirect:/supervisor-panel/management";
    }

    @GetMapping("/edit-position/{id}")
    public String showEditPositionForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Position position = positionService.findPositionById(id);

        if(position == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono stanowiska o ID: " + id);
            return "redirect:/error-handler";
        }

        model.addAttribute("position", position);

        List<Skill> skills = skillService.findAllSkills();
        model.addAttribute("skills", skills);

        List<Qualification> qualifications = qualificationService.findAllQualifications();
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/positions/edit";
    }

    @PostMapping("/edit-position")
    public String updatePosition(@ModelAttribute @Valid Position position,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("id", position.getId());
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/edit-position/{id}";
        }

        Position positionBeforeUpdate = positionService.findPositionById(position.getId());

        if (positionBeforeUpdate == null) {
            redirectAttributes.addAttribute("id", position.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono stanowiska o ID: " + position.getId());
            return "redirect:/supervisor-panel/management/edit-position/{id}";
        }


        boolean isPositionNameOccupied = positionService.isPositionNameOccupied(position);
        if(isPositionNameOccupied) {
            redirectAttributes.addAttribute("id", position.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Stanowisko o podanej nazwie już istnieje");
            return "redirect:/supervisor-panel/management/edit-position/{id}";
        }


        position.setIsActive(positionBeforeUpdate.getIsActive());
        position.setTemporary(positionBeforeUpdate.isTemporary());

        if (position.getSkill() == null) {
            position.setSkill(null);
        } else {
            if (positionBeforeUpdate.getSkill() != null && positionBeforeUpdate.getSkill().getId() != position.getSkill().getId()) {
                Skill skill = skillService.findSkillById(position.getSkill().getId());
                position.setSkill(skill);
            } else {
                position.setSkill(positionBeforeUpdate.getSkill());
            }
        }

        int count = 0;

        Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
        Qualification idCardQualification = qualificationService.findQualificationByName("stała przepustka");

        List<Qualification> qualifications = new ArrayList<>();
        if (position.getNeededQualifications() != null) {
            for (Qualification qualification : position.getNeededQualifications()) {
                Qualification nextQualification = qualificationService.findQualificationById(qualification.getId());
                if (nextQualification != null) {
                    if (noIdCardQualification != null && idCardQualification != null &&
                            (nextQualification.getName().equals(noIdCardQualification.getName()) || nextQualification.getName().equals(idCardQualification.getName()))) {
                        count++;
                    }
                    qualifications.add(nextQualification);
                }
            }
        }

        if (count == 2) {
            redirectAttributes.addAttribute("id", position.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie można jednocześnie wybrać opcji 'stała przepustka' i 'brak stałej przepustki'.");
            return "redirect:/supervisor-panel/management/edit-position/{id}";
        }

        position.setNeededQualifications(qualifications);

        List<Employee> employees = positionBeforeUpdate.getEmployees() != null ? new ArrayList<>(positionBeforeUpdate.getEmployees()) : new ArrayList<>();
        List<Employee> employeesAfterRemoveIteration = new ArrayList<>();

        Position breakPosition = positionService.findPositionByName("przerwa");

        if (breakPosition != null) {
            for (Employee employee : employees) {
                if (!employee.getQualifications().containsAll(position.getNeededQualifications())) {
                    PositionEmployeeHistory history = historyService.findByEmployeeIdAndIsActiveTrue(employee.getId());

                    if (history != null) {
                        history.setEndTime(LocalTime.now());
                        history.setActive(false);
                        historyService.updateHistory(history);
                    }

                    PositionEmployeeHistory newHistory = new PositionEmployeeHistory(employee, breakPosition);
                    historyService.addHistory(newHistory);

                    employee.setPosition(breakPosition);
                    employeeService.updateEmployee(employee);
                } else {
                    employeesAfterRemoveIteration.add(employee);
                }
            }
        }

        position.setEmployees(employeesAfterRemoveIteration);
        positionService.updatePosition(position);

        return "redirect:/supervisor-panel/management";
    }

    @PostMapping("/delete-position/{id}")
    public String deletePosition(@PathVariable("id") int id,
                                 @RequestParam("password") String password,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User currentUser = userService.findUserByUsername(username);

        if (currentUser == null || !passwordEncoder.matches(password, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podane hasło jest nieprawidłowe. Usunięcie nie zostało wykonane.");
            return "redirect:/supervisor-panel/management";
        }

        Position position = positionService.findPositionById(id);
        if (position == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono stanowiska.");
            return "redirect:/supervisor-panel/management";
        }

        if(!position.getEmployees().isEmpty()){
            Position breakPosition = positionService.findPositionByName("przerwa");
            for (Employee activeEmployee:position.getEmployees()) {
                activeEmployee.setPosition(breakPosition);
                employeeService.updateEmployee(activeEmployee);

                PositionEmployeeHistory history = historyService.findByEmployeeIdAndIsActiveTrue(activeEmployee.getId());
                history.setActive(false);
                history.setEndTime(LocalTime.now());
                historyService.updateHistory(history);

                PositionEmployeeHistory newHistory = new PositionEmployeeHistory(activeEmployee, breakPosition);
                historyService.addHistory(newHistory);
            }
        }
        List<PositionEmployeeHistory> historyToRemove = historyService.findAllByPositionId(position.getId());
        for(PositionEmployeeHistory history:historyToRemove){
            historyService.deleteHistoryById(history.getId());
        }

        positionService.deletePositionById(id);
        return "redirect:/supervisor-panel/management";
    }

}
