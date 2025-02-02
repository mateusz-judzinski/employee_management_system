package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.*;
import employee.management.system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public PositionManagementController(SkillService skillService, QualificationService qualificationService, PositionService positionService, PositionEmployeeHistoryService historyService, EmployeeService employeeService) {
        this.skillService = skillService;
        this.qualificationService = qualificationService;
        this.positionService = positionService;
        this.historyService = historyService;
        this.employeeService = employeeService;
    }

    @GetMapping("/add-position")
    public String showPositionForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Position position = new Position();
        model.addAttribute("position", position);

        List<Skill> skill = skillService.findAllSkills();
        model.addAttribute("skills", skill);

        List<Qualification> qualifications = qualificationService.findAllQualifications();
        Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
        qualifications.remove(noIdCardQualification);
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/positions/form";
    }

    @PostMapping("/add-position")
    public String addPosition(@ModelAttribute("position") Position position,
                              RedirectAttributes redirectAttributes) {

        boolean positionExists = positionService.existsByPositionName(position.getPositionName());

        if (positionExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stanowisko o podanej nazwie już istnieje.");
            return "redirect:/supervisor-panel/management/add-position";
        }

        if(position.getSkill() != null){
            Skill skill = skillService.findSkillById(position.getSkill().getId());
            position.setSkill(skill);
        }

        List<Qualification> selectedQualifications = new ArrayList<>();

        if(position.getNeededQualifications().isEmpty() || position.getNeededQualifications() == null){
            Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
            selectedQualifications.add(noIdCardQualification);
            position.setNeededQualifications(selectedQualifications);
        }

        else{
            List<Integer> qualificationIds = new ArrayList<>();
            for (Qualification qualification : position.getNeededQualifications()) {
                qualificationIds.add(qualification.getId());
            }

            for (Integer id:qualificationIds){
                Qualification nextQualification = qualificationService.findQualificationById(id);
                selectedQualifications.add(nextQualification);
            }

            position.setNeededQualifications(selectedQualifications);
        }

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
        Qualification noIdCard = qualificationService.findQualificationByName("brak stałej przepustki");
        qualifications.remove(noIdCard);
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/positions/edit";
    }

    @PostMapping("/edit-position")
    public String updatePosition(@ModelAttribute("position") Position position, RedirectAttributes redirectAttributes){

        Position positionBeforeUpdate = positionService.findPositionById(position.getId());

        if(positionBeforeUpdate == null){
            redirectAttributes.addAttribute("id", position.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono stanowiska o ID: " + position.getId());
            return "redirect:/supervisor-panel/management/edit-employee/{id}";
        }

        position.setIsActive(positionBeforeUpdate.getIsActive());
        position.setTemporary(positionBeforeUpdate.isTemporary());

        if(position.getSkill() == null){
            position.setSkill(null);
        }
        else{
            if(positionBeforeUpdate.getSkill().getId() != position.getSkill().getId()){
                Skill skill = skillService.findSkillById(position.getSkill().getId());
                position.setSkill(skill);
            } else {
                position.setSkill(positionBeforeUpdate.getSkill());
            }
        }

        boolean containsIdCardQualification = false;

        List<Qualification> qualifications = new ArrayList<>();
        List<Integer> qualificationsIds = new ArrayList<>();
        for (Qualification qualification:position.getNeededQualifications()){
            qualificationsIds.add(qualification.getId());
        }
        for (Integer id:qualificationsIds) {
            Qualification nextQualification = qualificationService.findQualificationById(id);
            if(Objects.equals(nextQualification.getName(), "stała przepustka")){
                containsIdCardQualification = true;
            }
            qualifications.add(nextQualification);
        }

        if(!containsIdCardQualification){
            Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
            qualifications.add(noIdCardQualification);
        }

        position.setNeededQualifications(qualifications);

        List<Employee> employees = positionBeforeUpdate.getEmployees();
        List<Employee> employeesAfterRemoveIteration = new ArrayList<>();
        Position breakPosition = positionService.findPositionByName("przerwa");
        for (Employee employee:employees) {
            if(!employee.getQualifications().containsAll(position.getNeededQualifications())){
                PositionEmployeeHistory history = historyService.findByEmployeeIdAndIsActiveTrue(employee.getId());
                history.setEndTime(LocalTime.now());
                history.setActive(false);
                historyService.updateHistory(history);

                PositionEmployeeHistory newHistory = new PositionEmployeeHistory(employee, breakPosition);
                historyService.addHistory(newHistory);

                employee.setPosition(breakPosition);
                employeeService.updateEmployee(employee);
            } else{
                employeesAfterRemoveIteration.add(employee);
            }
        }
        position.setEmployees(employeesAfterRemoveIteration);

        positionService.updatePosition(position);

        return "redirect:/supervisor-panel/management";
    }
}
