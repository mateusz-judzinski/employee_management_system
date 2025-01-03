package employee.management.system.controller;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.PositionEmployeeHistoryService;
import employee.management.system.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/leader-panel")
public class LeaderController {

    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final PositionEmployeeHistoryService historyService;

    @Autowired
    public LeaderController(EmployeeService employeeService, PositionService positionService, PositionEmployeeHistoryService historyService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.historyService = historyService;
    }

    @GetMapping()
    public String getLeaderPanel(Model model){

        Position breakPosition = positionService.findPositionByName("przerwa");
        model.addAttribute("breakPosition", breakPosition);

        List<Position> positions = positionService.findAllPositions();
        positions.remove(breakPosition);
        model.addAttribute("positions", positions);

        Map<Integer, LocalDateTime> activeEmployees = historyService.findAllActiveEmployeesAndStartTimeDate();
        model.addAttribute("activeEmployees", activeEmployees);

        return "leader/panel";
    }

    @PostMapping("/position/add-employee/{positionId}")
    public String addEmployeeToPosition(@PathVariable("positionId") int positionId,
                                        @RequestParam(value = "employeesIds", required = false) List<Integer> employeesIds){

        if(employeesIds == null || employeesIds.isEmpty()){
            return "redirect:/leader-panel";
        }

        positionService.addEmployeesIntoPosition(positionId, employeesIds);
        return "redirect:/leader-panel";
    }

    @GetMapping("/position/remove-employee/{employeeId}")
    public String removeEmployeeFromPosition(@PathVariable("employeeId") int employeeId){
        positionService.removeEmployeeFromPositionByEmployeeId(employeeId);

        return "redirect:/leader-panel";
    }
    @GetMapping("/position/switch-active/{positionId}")
    public String switchActiveStatus(@PathVariable("positionId") int positionId){
        positionService.switchActiveStatusByPositionId(positionId);

        return "redirect:/leader-panel";
    }

    @GetMapping("/employees")
    public String getShiftEmployeesForToday(Model model){

        LocalDate workDate = LocalDate.now();
        List<Employee> employees = employeeService.getShiftEmployees(workDate);
        model.addAttribute("employees", employees);

        return "leader/todays-employees";
    }

}
