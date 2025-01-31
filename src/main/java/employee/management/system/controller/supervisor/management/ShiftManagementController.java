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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String importSchedulePage(Model model, @RequestParam(value = "message", required = false) String message) {

        if(message != null){
            model.addAttribute("message", message);
        }

        return "supervisor/shifts/import-schedule";
    }

    @PostMapping("/import-schedule")
    public String importSchedule(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        int month = LocalDate.now().plusMonths(1).getMonthValue();
        int year = LocalDate.now().plusMonths(1).getYear();
        List<Shift> backUpShifts = shiftService.getShiftsForMonthAndYear(month, year);

        try {
            shiftService.importSchedule(file);
            redirectAttributes.addFlashAttribute("message", "Grafik został pomyślnie zaimportowany!");
        } catch (Exception e) {
            shiftService.saveAll(backUpShifts);
            redirectAttributes.addFlashAttribute("message",
                    "Wystąpił błąd podczas importowania pliku. Upewnij się, że plik jest prawidłowy, jego zawartość dobrze sformatowana i spróbuj ponownie.");
        }
        return "redirect:/supervisor-panel/management/import-schedule";
    }

    @GetMapping("/add-shift")
    public String showShiftForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {

        Shift shift = new Shift();
        List<Employee> employees = employeeService.getAllEmployeesSortedByLastName();

        model.addAttribute("shift", shift);
        model.addAttribute("employees", employees);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/shifts/form";
    }

    @PostMapping("/add-shift")
    public String addShift(@ModelAttribute("shift") Shift shift, RedirectAttributes redirectAttributes) {

        LocalDate shiftDate = shift.getWorkDate();
        LocalDate now = LocalDate.now();
        LocalTime shiftEndTime = shift.getEndTime();
        Employee shiftEmployee = shift.getEmployee();
        boolean alreadyHaveShiftThatDay = shiftService.doesEmployeeAlreadyHaveShiftInProvidedDay(shiftDate, shiftEmployee);

        if (alreadyHaveShiftThatDay) {
            String errorMessage = "Pracownik posiada już przypisaną zmianę w podany dzień";
            redirectAttributes.addAttribute("errorMessage", errorMessage);

            return "redirect:/supervisor-panel/management/add-shift";

        } else if (shiftDate.isBefore(now) ||
                (shiftDate.isEqual(now) &&
                        shiftEndTime.isBefore(LocalTime.now()) &&
                        shiftEndTime.isAfter(shift.getStartTime()))) {

            String errorMessage = "Nie można dodawać zmian na przeszłe dni";
            redirectAttributes.addAttribute("errorMessage", errorMessage);
            return "redirect:/supervisor-panel/management/add-shift";
        }

        Employee employee = employeeService.findEmployeeById(shiftEmployee.getId());
        shift.setEmployee(employee);
        shift.setActive(true);
        shift.setShiftName(shift.getShiftNameByShiftStartTime(shift.getStartTime()));

        shiftService.addShift(shift);

        return "redirect:/supervisor-panel/management";
    }
}
