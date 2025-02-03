package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.entity.Qualification;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.PositionEmployeeHistoryService;
import employee.management.system.service.PositionService;
import employee.management.system.service.QualificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class QualificationManagementController {

    final private EmployeeService employeeService;
    final private PositionService positionService;
    final private QualificationService qualificationService;
    final private PositionEmployeeHistoryService historyService;

    public QualificationManagementController(EmployeeService employeeService, PositionService positionService, QualificationService qualificationService, PositionEmployeeHistoryService historyService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.qualificationService = qualificationService;
        this.historyService = historyService;
    }

    @GetMapping("/add-qualification")
    public String showQualificationForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Qualification qualification = new Qualification();
        model.addAttribute("qualification", qualification);

        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();
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

    @GetMapping("/edit-qualification/{id}")
    public String showEditQualificationForm(@PathVariable("id") int id, Model model,
                                            RedirectAttributes redirectAttributes,
                                            @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        Qualification qualification = qualificationService.findQualificationById(id);

        if (qualification == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono kwalifikacji o ID: " + id);
            return "redirect:/error-handler";
        }

        model.addAttribute("qualification", qualification);

        List<Employee> allEmployees = employeeService.getAllEmployeesSortedByLastName();
        model.addAttribute("allEmployees", allEmployees);

        List<Position> allPositions = positionService.findAllPositions();
        model.addAttribute("allPositions", allPositions);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/qualifications/edit";
    }

    @PostMapping("/edit-qualification")
    public String updateQualification(@ModelAttribute("qualification") Qualification qualification,
                                      RedirectAttributes redirectAttributes) {

        Qualification managedQualification = qualificationService.findQualificationById(qualification.getId());

        if (managedQualification == null) {
            redirectAttributes.addAttribute("id", qualification.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono kwalifikacji o ID: " + qualification.getId());
            return "redirect:/supervisor-panel/management/edit-qualification/{id}";
        }

        List<Employee> previousEmployees = employeeService.findEmployeesByQualificationId(qualification.getId());

        for (Employee previousEmployee : previousEmployees) {
            previousEmployee.getQualifications().remove(managedQualification);
        }

        if (qualification.getEmployees() != null) {
            List<Employee> employees = new ArrayList<>();
            for (Employee employee : qualification.getEmployees()) {
                Employee nextEmployee = employeeService.findEmployeeById(employee.getId());
                if (nextEmployee != null && !nextEmployee.getQualifications().contains(managedQualification)) {
                    employees.add(nextEmployee);
                    nextEmployee.getQualifications().add(managedQualification);
                }
            }
            managedQualification.setEmployees(employees);
        } else {
            managedQualification.setEmployees(new ArrayList<>());
        }


        List<Position> previousPositions = positionService.findPositionByQualificationId(qualification.getId());

        for (Position previousPosition : previousPositions) {
            previousPosition.getNeededQualifications().remove(managedQualification);
        }

        if (qualification.getPositions() != null) {
            List<Position> positions = new ArrayList<>();
            for (Position position : qualification.getPositions()) {
                Position nextPosition = positionService.findPositionById(position.getId());
                if (nextPosition != null && !nextPosition.getNeededQualifications().contains(managedQualification)) {
                    positions.add(nextPosition);
                    nextPosition.getNeededQualifications().add(managedQualification);
                }
            }
            managedQualification.setPositions(positions);
        } else {
            managedQualification.setPositions(new ArrayList<>());
        }

        qualificationService.updateQualification(managedQualification);

        Position breakPosition = positionService.findPositionByName("przerwa");
        for (Position position:managedQualification.getPositions()) {
            for(Employee employee:position.getEmployees()){
                if(!employee.getQualifications().contains(managedQualification)){
                    PositionEmployeeHistory history = historyService.findByEmployeeIdAndIsActiveTrue(employee.getId());
                    history.setEndTime(LocalTime.now());
                    history.setActive(false);
                    historyService.updateHistory(history);

                    employee.setPosition(breakPosition);
                    employeeService.updateEmployee(employee);

                    PositionEmployeeHistory newHistory = new PositionEmployeeHistory(employee, breakPosition);
                    historyService.addHistory(newHistory);
                }
            }
        }


        return "redirect:/supervisor-panel/management";
    }



}
