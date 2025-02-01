package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.Qualification;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.PositionService;
import employee.management.system.service.QualificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class QualificationManagementController {

    final private EmployeeService employeeService;
    final private PositionService positionService;
    final private QualificationService qualificationService;

    public QualificationManagementController(EmployeeService employeeService, PositionService positionService, QualificationService qualificationService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.qualificationService = qualificationService;
    }

    @GetMapping("/add-qualification")
    public String showQualificationForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Qualification qualification = new Qualification();
        model.addAttribute("qualification", qualification);

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        List<Position> positions = positionService.getPositionsForManagement();
        model.addAttribute("positions", positions);

        if(errorMessage != null){
            model.addAttribute(errorMessage);
        }

        return "supervisor/qualifications/form";
    }

    @PostMapping("/add-qualification")
    public String addQualification(@ModelAttribute("qualification") Qualification qualification,
                                   RedirectAttributes redirectAttributes) {

        boolean qualificationExists = qualificationService.existsByName(qualification.getName());
        if (qualificationExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kwalifikacja o podanej nazwie już istnieje.");
            return "redirect:/supervisor-panel/management/add-qualification";
        }

        qualificationService.addQualification(qualification);

        List<Employee> selectedEmployees = new ArrayList<>();
        List<Integer> employeeIds = new ArrayList<>();
        for(Employee employee:qualification.getEmployees()){
            employeeIds.add(employee.getId());
        }

        for(Integer id:employeeIds){
            Employee nextEmployee = employeeService.findEmployeeById(id);
            selectedEmployees.add(nextEmployee);
            nextEmployee.getQualifications().add(qualification);
            employeeService.updateEmployee(nextEmployee);
        }
        qualification.setEmployees(selectedEmployees);


        List<Position> selectedPositions = new ArrayList<>();
        List<Integer> positionsIds = new ArrayList<>();
        for(Position position:qualification.getPositions()){
            positionsIds.add(position.getId());
        }

        for(Integer id:positionsIds){
            Position nextPosition = positionService.findPositionById(id);
            selectedPositions.add(nextPosition);
            nextPosition.getNeededQualifications().add(qualification);
            positionService.updatePosition(nextPosition);
        }
        qualification.setPositions(selectedPositions);


        return "redirect:/supervisor-panel/management";
    }

}
