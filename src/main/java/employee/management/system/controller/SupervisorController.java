package employee.management.system.controller;

import employee.management.system.dto.AveragePositionTime;
import employee.management.system.entity.*;
import employee.management.system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/supervisor-panel")
public class SupervisorController {

    private final ShiftService shiftService;
    private final PositionService positionService;
    private final PositionEmployeeHistoryService historyService;
    private final EmployeeService employeeService;
    private final EmployeeSkillService employeeSkillService;

    public SupervisorController(ShiftService shiftService, PositionService positionService, PositionEmployeeHistoryService historyService, EmployeeService employeeService, EmployeeSkillService employeeSkillService) {
        this.shiftService = shiftService;
        this.positionService = positionService;
        this.historyService = historyService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
    }

    @GetMapping()
    public String getSupervisorPanel(Model model) {

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

        return "supervisor/panel";
    }

    @GetMapping("/schedules/list")
    public String getSchedule(@RequestParam(value = "date", required = false) String date, Model model) {
        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<Shift> allShifts = shiftService.getEntireDayScheduleByWorkDate(selectedDate);
        List<List<Shift>> splitShifts = shiftService.splitOnActiveAndInactive(allShifts);
        model.addAttribute("activeShifts", splitShifts.get(0));
        model.addAttribute("inactiveShifts", splitShifts.get(1));
        model.addAttribute("selectedDate", selectedDate);
        return "supervisor/schedules/list";
    }

    @GetMapping("/employees/list")
    public String getAllEmployees(Model model){

        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();
        model.addAttribute("employees", employees);

        boolean todays = false;
        model.addAttribute("todays", todays);

        return "supervisor/employees/list";
    }

    @GetMapping("/employees/today")
    public String getTodaysEmployees(Model model){

        List<Employee> employees = employeeService.findEmployeesWithCurrentShift();
        model.addAttribute("employees", employees);

        boolean todays = true;
        model.addAttribute("todays", todays);

        return "supervisor/employees/list";
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

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<Shift> thisMonthShifts = shiftService.getMonthShiftForEmployeeById(employeeId, month, year);
        model.addAttribute("thisMonthShifts", thisMonthShifts);

        return "supervisor/employees/details";
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

        return "supervisor/employees/list";
    }
}
