package employee.management.system.controller.supervisor.management;

import employee.management.system.dto.NewEmployeeDTO;
import employee.management.system.entity.Employee;
import employee.management.system.entity.EmployeeSkill;
import employee.management.system.entity.Qualification;
import employee.management.system.entity.Skill;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.EmployeeSkillService;
import employee.management.system.service.QualificationService;
import employee.management.system.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class EmployeeManagementController {

    private final SkillService skillService;
    private final EmployeeService employeeService;
    private final EmployeeSkillService employeeSkillService;
    private final QualificationService qualificationService;

    public EmployeeManagementController(SkillService skillService, EmployeeService employeeService, EmployeeSkillService employeeSkillService, QualificationService qualificationService) {
        this.skillService = skillService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
        this.qualificationService = qualificationService;
    }

    @GetMapping("/add-employee")
    public String showAddEmployeeForm(Model model){

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

        return "supervisor/employees/form";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute NewEmployeeDTO newEmployee) {

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

}
