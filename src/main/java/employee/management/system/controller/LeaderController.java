package employee.management.system.controller;

import employee.management.system.entity.Employee;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/leader-panel")
public class LeaderController {

    private final EmployeeService employeeService;

    @Autowired
    public LeaderController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showLeaderPanelPage(){
        return "leader-panel-page";
    }

    @GetMapping("/employees")
    public String showShiftEmployeesPage(Model model){
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        return "shift-employees-page";
    }
}
