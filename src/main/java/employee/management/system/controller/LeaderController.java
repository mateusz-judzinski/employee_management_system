package employee.management.system.controller;

import employee.management.system.dto.AveragePositionTime;
import employee.management.system.entity.*;
import employee.management.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/leader-panel")
public class LeaderController {

    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final PositionEmployeeHistoryService historyService;
    private final ShiftService shiftService;
    private final EmployeeSkillService employeeSkillService;

    @Autowired
    public LeaderController(EmployeeService employeeService, PositionService positionService, PositionEmployeeHistoryService historyService, ShiftService shiftService, EmployeeSkillService employeeSkillService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.historyService = historyService;
        this.shiftService = shiftService;
        this.employeeSkillService = employeeSkillService;
    }

    @GetMapping()
    public String getLeaderPanel(Model model){

        shiftService.updateEmployeesWithCurrentShift();

        Position breakPosition = positionService.findPositionByName("przerwa");
        model.addAttribute("breakPosition", breakPosition);

        Position otherPosition = positionService.findPositionByName("inne");

        List<Position> positions = positionService.findAllPositions();
        positions.remove(breakPosition);
        positions.remove(otherPosition);
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

    @PostMapping("/position/add-temporary")
    public String addTemporaryPosition(@RequestParam String positionName, @RequestParam String description){
        positionService.addTemporaryPosition(positionName, description);

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
    public String getAllEmployees(Model model){

        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();
        model.addAttribute("employees", employees);

        boolean todays = false;
        model.addAttribute("todays", todays);

        return "leader/employees";
    }

    @GetMapping("/employees/today")
    public String getTodaysEmployees(Model model){

        List<Employee> employees = employeeService.findEmployeesWithCurrentShift();
        model.addAttribute("employees", employees);

        boolean todays = true;
        model.addAttribute("todays", todays);

        return "leader/employees";
    }

    @GetMapping("/employees/details/{employeeId}")
    public String getDetailsForEmployee(@PathVariable("employeeId") int employeeId, Model model){

        Employee employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);

        List<EmployeeSkill> skills = employeeSkillService.findAllEmployeeSkillByEmployeeId(employeeId);
        model.addAttribute("skills", skills);

        List<AveragePositionTime> averagePositionTimes = historyService.calculateEmployeePositionRatioPercentage(employeeId);
        model.addAttribute("averagePositionTimes", averagePositionTimes);

        List<PositionEmployeeHistory> todaysHistory = historyService.findTodaysActivityByEmployeeId(employeeId);
        model.addAttribute("todaysHistory", todaysHistory);

        return "leader/employee-details";
    }

    @GetMapping("/employees/search")
    public String getEmployeesSearchBar(@RequestParam(value = "searchData", required = false) String searchData, Model model){

        List<Employee> employees;

        if(searchData == null || searchData.isBlank()){
            employees = employeeService.getAllEmployeesSortedByLastName();
        } else{
            String[] employee = searchData.split(" ");
            if(employee.length == 1){
                String name = employee[0];
                employees = employeeService.getEmployeesFromSearchBarByOneElement(name, name);

            } else if (employee.length == 2){
                String firstName = employee[0];
                String lastName = employee[1];
                employees = employeeService.getEmployeesFromSearchBarByTwoElements(firstName, lastName);

            } else{
                employees = new ArrayList<>();
            }

        }

        boolean todays = false;

        model.addAttribute("todays", todays);
        model.addAttribute("employees", employees);
        model.addAttribute("searchData", searchData);

        return "leader/employees";
    }

    @GetMapping("/schedule")
    public String getSchedule(@RequestParam(value = "date", required = false) String date, Model model) {
        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<Shift> allShifts = shiftService.getEntireDayScheduleByWorkDate(selectedDate);
        List<List<Shift>> splitShifts = shiftService.splitOnActiveAndInactive(allShifts);
        model.addAttribute("activeShifts", splitShifts.get(0));
        model.addAttribute("inactiveShifts", splitShifts.get(1));
        model.addAttribute("selectedDate", selectedDate);
        return "leader/schedule";
    }
}
