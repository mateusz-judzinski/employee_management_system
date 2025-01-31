package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Shift;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.ShiftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class ShiftManagementController {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;

    public ShiftManagementController(ShiftService shiftService, EmployeeService employeeService) {
        this.shiftService = shiftService;
        this.employeeService = employeeService;
    }

    @GetMapping("/import-schedule")
    public String importSchedulePage() {
        return "supervisor/shifts/import-schedule";
    }

    @PostMapping("/import-schedule")
    public String importSchedule(@RequestParam("file") MultipartFile file, Model model) {
        try {
            shiftService.importSchedule(file);
        } catch (IOException e) {
            String error = "An error occurred: " + e.getMessage();
            model.addAttribute("error", error);
            return "user/error";
        }
        return "redirect:/supervisor-panel/management/import";
    }

    @GetMapping("/add-shift")
    public String showShiftForm(Model model) {

        Shift shift = new Shift();
        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();

        model.addAttribute("shift", shift);
        model.addAttribute("employees", employees);

        return "supervisor/shifts/form";
    }

    @PostMapping("/add-shift")
    public String addShift(@ModelAttribute("shift") Shift shift, Model model) {

        LocalDate shiftDate = shift.getWorkDate();
        LocalDate now = LocalDate.now();
        LocalTime shiftEndTime = shift.getEndTime();
        Employee shiftEmployee = shift.getEmployee();
        boolean alreadyHaveShiftThatDay = shiftService.doesEmployeeAlreadyHaveShiftInProvidedDay(shiftDate, shiftEmployee);

        if (alreadyHaveShiftThatDay) {
            String errorMessage = "Pracownik posiada już przypisaną zmianę w podany dzień";
            model.addAttribute("errorMessage", errorMessage);

            Shift newShift = new Shift();
            List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();

            model.addAttribute("shift", newShift);
            model.addAttribute("employees", employees);

            return "supervisor/shifts/form";

        } else if (shiftDate.isBefore(now) ||
                (shiftDate.isEqual(now) &&
                        shiftEndTime.isBefore(LocalTime.now()) &&
                        shiftEndTime.isAfter(shift.getStartTime()))) {
            String errorMessage = "Nie można dodawać zmian na przeszłe dni";
            model.addAttribute("errorMessage", errorMessage);

            Shift newShift = new Shift();
            List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();

            model.addAttribute("shift", newShift);
            model.addAttribute("employees", employees);

            return "supervisor/shifts/form";
        }

        Employee employee = employeeService.findEmployeeById(shiftEmployee.getId());
        shift.setEmployee(employee);
        shift.setActive(true);
        shift.setShiftName(shift.getShiftNameByShiftStartTime(shift.getStartTime()));

        shiftService.addShift(shift);

        return "redirect:/supervisor-panel/management";
    }
}
