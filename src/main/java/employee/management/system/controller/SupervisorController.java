package employee.management.system.controller;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Shift;
import employee.management.system.entity.User;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.ShiftService;
import employee.management.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel")
public class SupervisorController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ShiftService shiftService;

    @Autowired
    public SupervisorController(UserService userService, EmployeeService employeeService, ShiftService shiftService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.shiftService = shiftService;
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

    @GetMapping("/employees/new")
    public String addNewEmployeePage(Model model){
        model.addAttribute("employee", new Employee());

        return "add-employee-form-page";
    }

    @PostMapping("/employees")
    public String saveNewEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.addEmployee(employee);

        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/employees/edit/{employeeId}")
    public String editEmployee(@PathVariable("employeeId") int employeeId, Model model){

        Employee employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);

        return "edit-employee-form-page";
    }

    @PostMapping("/employees/update")
    public String updateEmployee(@ModelAttribute("employee") Employee employee){

        employeeService.updateEmployee(employee);
        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/employees/delete/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId){

        employeeService.deleteEmployeeById(employeeId);
        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/leaders")
    public String showLeadersPage(Model model){
        List<User> leaders = userService.findByRole("ROLE_LEADER");
        model.addAttribute("leaders", leaders);

        return "all-leaders-page";
    }

    @GetMapping("/leaders/new")
    public String addNewLeader(Model model){
        model.addAttribute("leader", new User());
        return "add-leader-form-page";
    }

    @PostMapping("/leaders")
    public String saveNewLeader(@ModelAttribute("leader") User leader){
        userService.addUser(leader);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/leaders/edit/{leaderId}")
    public String editLeader(@PathVariable("leaderId") int leaderId, Model model){
        User leader = userService.findUserById(leaderId);
        model.addAttribute("leader", leader);

        return "edit-leader-form-page";
    }

    @PostMapping("/leaders/update")
    public String saveLeader(@ModelAttribute("leader") User leader){
        userService.updateUser(leader);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/leaders/delete/{leaderId}")
    public String deleteLeader(@PathVariable("leaderId") int leaderId){
        userService.deleteUserById(leaderId);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/month-schedule")
    public String getScheduleForThisMonth(Model model){
        int month = LocalDate.now().getMonthValue();

        List<Shift> shifts = shiftService.getScheduleForMonth(month);
        model.addAttribute("shifts", shifts);

        return "employee-month-schedule-page";
    }

    @GetMapping("/month-schedule/{month}")
    public String getScheduleForProvidedMonth(@PathVariable("month") int month, Model model){
        List<Shift> shifts = shiftService.getScheduleForMonth(month);
        model.addAttribute("shifts", shifts);

        return "employee-month-schedule-page";
    }

    @GetMapping("/day-schedule")
    public String getScheduleForThisDay(@RequestParam(value = "day", required = false) Integer day, Model model) {
        int dayToUse = (day != null) ? day : LocalDate.now().getDayOfMonth();

        List<Shift> shifts = shiftService.getScheduleForDay(dayToUse);
        model.addAttribute("shifts", shifts);
        model.addAttribute("day", dayToUse);

        return "employee-day-schedule-page";
    }

}
