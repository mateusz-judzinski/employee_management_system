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
@RequestMapping("/supervisor-panel")
public class SupervisorController {

    private final UserService userService;
    private final EmployeeService employeeService;

    @Autowired
    public SupervisorController(UserService userService, EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showSupervisorPanelPage(){
        return "supervisor-panel-page";
    }

    @GetMapping("/employees")
    public String showEmployeesPage(Model model){
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        return "all-employees-page";
    }
}
