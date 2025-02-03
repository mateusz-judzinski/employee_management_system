package employee.management.system.controller.supervisor;

import employee.management.system.dto.AveragePositionTime;
import employee.management.system.entity.*;
import employee.management.system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final QualificationService qualificationService;
    private final SkillService skillService;
    private final UserService userService;

    public SupervisorController(ShiftService shiftService, PositionService positionService, PositionEmployeeHistoryService historyService, EmployeeService employeeService, EmployeeSkillService employeeSkillService, QualificationService qualificationService, SkillService skillService, UserService userService) {
        this.shiftService = shiftService;
        this.positionService = positionService;
        this.historyService = historyService;
        this.employeeService = employeeService;
        this.employeeSkillService = employeeSkillService;
        this.qualificationService = qualificationService;
        this.skillService = skillService;
        this.userService = userService;
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
        return "supervisor/shifts/list";
    }

    @GetMapping("/employees/list")
    public String getAllEmployees(Model model) {

        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();
        model.addAttribute("employees", employees);

        boolean todays = false;
        model.addAttribute("todays", todays);

        return "supervisor/employees/list";
    }

    @GetMapping("/employees/today")
    public String getTodaysEmployees(Model model) {

        List<Employee> employees = employeeService.findEmployeesWithCurrentShift();
        model.addAttribute("employees", employees);

        boolean todays = true;
        model.addAttribute("todays", todays);

        return "supervisor/employees/list";
    }

    @GetMapping("/employees/details/{employeeId}")
    public String getDetailsForEmployee(@PathVariable("employeeId") int employeeId, Model model) {

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
    public String getEmployeesSearchBar(@RequestParam(value = "searchData", required = false) String searchData, Model model) {

        List<Employee> employees;

        if (searchData == null || searchData.isBlank()) {
            employees = employeeService.getAllEmployeesSortedByLastName();
        } else {
            String[] employee = searchData.split(" ");
            if (employee.length == 1) {
                String name = employee[0];
                employees = employeeService.getEmployeesFromSearchBarByOneElement(name, name);

            } else if (employee.length == 2) {
                String firstName = employee[0];
                String lastName = employee[1];
                employees = employeeService.getEmployeesFromSearchBarByTwoElements(firstName, lastName);

            } else {
                employees = new ArrayList<>();
            }

        }

        boolean todays = false;

        model.addAttribute("todays", todays);
        model.addAttribute("employees", employees);
        model.addAttribute("searchData", searchData);

        return "supervisor/employees/list";
    }

    @GetMapping("/management/search")
    public String getEmployeesManagementSearchBar(@RequestParam(value = "searchData", required = false) String searchData,
                                                  @RequestParam(value = "date", required = false) String date, Model model) {

        List<Employee> employees = searchEmployees(searchData);

        boolean searchPerformed = true;
        boolean todays = false;

        model.addAttribute("todays", todays);
        model.addAttribute("searchPerformed", searchPerformed);
        model.addAttribute("employees", employees);
        model.addAttribute("searchData", searchData);

        loadManagementModel(model, date);


        return "supervisor/management";
    }

    @GetMapping("/management")
    public String getManagementPage(@RequestParam(value = "date", required = false) String date, Model model,
                                    @RequestParam(value = "errorMessage", required = false) String errorMessage) {

        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();
        model.addAttribute("employees", employees);
        model.addAttribute("searchPerformed", false);

        loadManagementModel(model, date);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/management";
    }

    private void loadManagementModel(Model model, String date) {
        List<Position> positions = positionService.getPositionsForManagement();
        model.addAttribute("positions", positions);

        List<Qualification> qualifications = qualificationService.getQualificationsForManagement();
        model.addAttribute("qualifications", qualifications);

        List<Skill> skills = skillService.findAllSkills();
        model.addAttribute("skills", skills);

        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<Shift> shifts = shiftService.getEntireDayScheduleByWorkDate(selectedDate);
        model.addAttribute("shifts", shifts);

        List<User> users = userService.findByRole("ROLE_LEADER");
        model.addAttribute("users", users);
    }

    private List<Employee> searchEmployees(String searchData) {

        if (searchData == null || searchData.isBlank()) {

            return employeeService.getAllEmployeesSortedByLastName();
        }

        String[] employee = searchData.split(" ");
        if (employee.length == 1) {
            String name = employee[0];

            return employeeService.getEmployeesFromSearchBarByOneElement(name, name);
        }

        else if (employee.length == 2) {

            String firstName = employee[0];
            String lastName = employee[1];
            return employeeService.getEmployeesFromSearchBarByTwoElements(firstName, lastName);
        }

        return new ArrayList<>();
    }
}
