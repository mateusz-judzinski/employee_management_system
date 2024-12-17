package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Employee;
import employee.management.system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/employees")
public class EmployeeManagementController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeManagementController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "supervisor/employee/list";
    }

    @GetMapping("/new")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "supervisor/employee/form";
    }

    @PostMapping
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.addEmployee(employee);
        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/edit/{employeeId}")
    public String showEditEmployeeForm(@PathVariable("employeeId") int employeeId, Model model) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        return "supervisor/employee/edit";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.updateEmployee(employee);
        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/delete/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return "redirect:/supervisor-panel/employees";
    }
}
