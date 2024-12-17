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
    public String getSupervisorPanel(){
        return "supervisor/panel";
    }

    @GetMapping("/employees")
    public String getAllEmployees(Model model){
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        return "supervisor/employee/list";
    }

    @GetMapping("/employees/new")
    public String showAddEmployeeForm(Model model){
        model.addAttribute("employee", new Employee());

        return "supervisor/employee/form";
    }

    @PostMapping("/employees")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.addEmployee(employee);

        return "redirect:/supervisor-panel/employees";
    }

    @GetMapping("/employees/edit/{employeeId}")
    public String showEditEmployeeForm(@PathVariable("employeeId") int employeeId, Model model){

        Employee employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);

        return "supervisor/employee/edit";
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
    public String getAllLeaders(Model model){
        List<User> leaders = userService.findByRole("ROLE_LEADER");
        model.addAttribute("leaders", leaders);

        return "supervisor/leader/list";
    }

    @GetMapping("/leaders/new")
    public String showAddLeaderForm(Model model){
        model.addAttribute("leader", new User());
        return "supervisor/leader/form";
    }

    @PostMapping("/leaders")
    public String saveLeader(@ModelAttribute("leader") User leader){
        userService.addUser(leader);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/leaders/edit/{leaderId}")
    public String showEditLeaderForm(@PathVariable("leaderId") int leaderId, Model model){
        User leader = userService.findUserById(leaderId);
        model.addAttribute("leader", leader);

        return "supervisor/leader/edit";
    }

    @PostMapping("/leaders/update")
    public String updateLeader(@ModelAttribute("leader") User leader){
        userService.updateUser(leader);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/leaders/delete/{leaderId}")
    public String deleteLeader(@PathVariable("leaderId") int leaderId){
        userService.deleteUserById(leaderId);
        return "redirect:/supervisor-panel/leaders";
    }

    @GetMapping("/month-schedule")
    public String getMonthlySchedule(@RequestParam(value = "month", required = false) Integer month, Model model){
        Map<String, List<String>> dailySchedule = shiftService.getScheduleForMonth(month);
        model.addAttribute("dailySchedule", dailySchedule);

        return "supervisor/schedule/month-schedule";
    }
    @GetMapping("/day-schedule")
    public String getDailySchedule(@RequestParam(value = "day", required = false) Integer day, Model model) {
        List<Shift> shifts = shiftService.getScheduleForDay(day);
        model.addAttribute("shifts", shifts);
        model.addAttribute("day", day);

        return "supervisor/schedule/day-schedule";
    }

    @GetMapping("/positions")
    public String getAllPositions(Model model){
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("positions", positions);

        return "supervisor/position/list";
    }

    @GetMapping("/positions/new")
    public String showAddPositionForm(Model model){
        model.addAttribute("position", new Position());

        return "supervisor/position/form";
    }

    @PostMapping("/positions")
    public String savePosition(@ModelAttribute("position") Position position){
        positionService.addPosition(position);

        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/positions/edit/{positionId}")
    public String showEditPositionForm(@PathVariable("positionId") int positionId, Model model){
        Position position = positionService.findPositionById(positionId);
        model.addAttribute("position", position);

        return "supervisor/position/edit";
    }

    @PostMapping("/positions/update")
    public String updatePosition(@ModelAttribute("position") Position position){
        positionService.updatePosition(position);

        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/positions/delete/{positionId}")
    public String deletePosition(@PathVariable("positionId") int positionId){
        positionService.deletePositionById(positionId);

        return "redirect:/supervisor-panel/positions";
    }


    @GetMapping("/skills")
    public String getAllSkills(Model model){
        List<Skill> skills = skillService.findAllSkills();
        model.addAttribute("skills", skills);

        return "supervisor/skill/list";
    }

    @GetMapping("/skills/new")
    public String showAddSkillForm(Model model){
        model.addAttribute("skill", new Skill());

        return "supervisor/skill/form";
    }

    @PostMapping("/skills")
    public String saveSkill(@ModelAttribute("skill") Skill skill){
        skillService.addSkill(skill);

        return "redirect:/supervisor-panel/skills";
    }

    @GetMapping("/skills/edit/{skillId}")
    public String showEditSkillForm(@PathVariable("skillId") int skillId, Model model){
        Skill skill = skillService.findSkillById(skillId);
        model.addAttribute("skill", skill);

        return "supervisor/skill/edit";
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
