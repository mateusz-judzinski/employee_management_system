package employee.management.system.controller;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/leader-panel")
public class LeaderController {

    private final EmployeeService employeeService;
    private final PositionService positionService;

    @Autowired
    public LeaderController(EmployeeService employeeService, PositionService positionService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
    }

    @GetMapping()
    public String getLeaderPanel(){
        return "leader/panel";
    }

    @GetMapping("/employees")
    public String getShiftEmployeesForToday(Model model){

        LocalDate workDate = LocalDate.now();
        List<Employee> employees = employeeService.getShiftEmployees(workDate);
        model.addAttribute("employees", employees);

        return "leader/todays-employees";
    }

    @GetMapping("/positions")
    public String getAllPositions(Model model){

        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("positions", positions);

        return "leader/todays-positions";

    }
}
