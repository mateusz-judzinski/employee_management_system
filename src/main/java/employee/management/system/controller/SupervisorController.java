package employee.management.system.controller;

import employee.management.system.entity.*;
import employee.management.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/supervisor-panel")
public class SupervisorController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final PositionService positionService;
    private final SkillService skillService;

    @Autowired
    public SupervisorController(UserService userService, EmployeeService employeeService, ShiftService shiftService, PositionService positionService, SkillService skillService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.positionService = positionService;
        this.skillService = skillService;
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
    public String getScheduleForThisMonth(@RequestParam(value = "month", required = false) Integer month, Model model){
        Map<String, List<String>> dailySchedule = shiftService.getScheduleForMonth(month);
        model.addAttribute("dailySchedule", dailySchedule);

        return "employee-month-schedule-page";
    }
    @GetMapping("/day-schedule")
    public String getScheduleForThisDay(@RequestParam(value = "day", required = false) Integer day, Model model) {
        List<Shift> shifts = shiftService.getScheduleForDay(day);
        model.addAttribute("shifts", shifts);
        model.addAttribute("day", day);

        return "employee-day-schedule-page";
    }

    @GetMapping("/positions")
    public String getPositionsList(Model model){
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("positions", positions);

        return "positions-management-page";
    }

    @GetMapping("/positions/new")
    public String addNewPosition(Model model){
        model.addAttribute("position", new Position());

        return "new-position-form-page";
    }

    @PostMapping("/positions")
    public String saveNewPosition(@ModelAttribute("position") Position position){
        positionService.addPosition(position);

        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/positions/edit/{positionId}")
    public String editPosition(@PathVariable("positionId") int positionId, Model model){
        Position position = positionService.findPositionById(positionId);
        model.addAttribute("position", position);

        return "edit-position-form-page";
    }

    @PostMapping("/positions/update")
    public String savePosition(@ModelAttribute("position") Position position){
        positionService.updatePosition(position);

        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/positions/delete/{positionId}")
    public String deletePosition(@PathVariable("positionId") int positionId){
        positionService.deletePositionById(positionId);

        return "redirect:/supervisor-panel/positions";
    }


    @GetMapping("/skills")
    public String getSkillsList(Model model){
        List<Skill> skills = skillService.findAllSkills();
        model.addAttribute("skills", skills);

        return "skills-page";
    }

    @GetMapping("/skills/new")
    public String addNewSkill(Model model){
        model.addAttribute("skill", new Skill());

        return "new-skill-form-page";
    }

    @PostMapping("/skills")
    public String saveNewSKill(@ModelAttribute("skill") Skill skill){
        skillService.addSkill(skill);

        return "redirect:/supervisor-panel/skills";
    }

    @GetMapping("/skills/edit/{skillId}")
    public String editSkill(@PathVariable("skillId") int skillId, Model model){
        Skill skill = skillService.findSkillById(skillId);
        model.addAttribute("skill", skill);

        return "edit-skill-form-page";
    }

    @PostMapping("/skills/update")
    public String updateSkill(@ModelAttribute("skill") Skill skill){
        skillService.updateSkill(skill);
        return "redirect:/supervisor-panel/skills";
    }

    @GetMapping("/skills/delete/{skillId}")
    public String deleteSkill(@PathVariable("skillId") int skillId){
        skillService.deleteSkillById(skillId);
        return "redirect:/supervisor-panel/skills";
    }
}
