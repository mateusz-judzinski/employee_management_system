package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Employee;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.entity.Shift;
import employee.management.system.service.EmployeeService;
import employee.management.system.service.PositionEmployeeHistoryService;
import employee.management.system.service.ShiftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class ShiftManagementController {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final PositionEmployeeHistoryService historyService;

    public ShiftManagementController(ShiftService shiftService, EmployeeService employeeService, PositionEmployeeHistoryService historyService) {
        this.shiftService = shiftService;
        this.employeeService = employeeService;
        this.historyService = historyService;
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
            return "redirect:/supervisor-panel/management";
        } catch (Exception e) {
            shiftService.saveAll(backUpShifts);
            redirectAttributes.addFlashAttribute("message",
                    "Wystąpił błąd podczas importowania pliku. Upewnij się, że plik jest prawidłowy, jego zawartość dobrze sformatowana i spróbuj ponownie");
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

        }
        if (shiftDate.isBefore(now) || (shiftDate.isEqual(now) &&
                shiftEndTime.isBefore(LocalTime.now()) &&
                shiftEndTime.isAfter(shift.getStartTime()))) {

            String errorMessage = "Dodawanie zmian w przeszłe dni jest zabronione";
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

    @GetMapping("/edit-shift/{id}")
    public String showEditShiftForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        Shift shift = shiftService.findShiftById(id);

        if (shift == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono zmiany o ID: " + id);
            return "redirect:/error";
        }

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("shift", shift);
        model.addAttribute("employees", employees);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/shifts/edit";
    }

    @PostMapping("/edit-shift")
    public String updateShift(@ModelAttribute("shift") Shift shift, RedirectAttributes redirectAttributes) {

        Shift shiftBeforeUpdate = shiftService.findShiftById(shift.getId());

        if (shiftBeforeUpdate == null) {
            redirectAttributes.addAttribute("id", shift.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono zmiany o ID: " + shift.getId());
            return "redirect:/supervisor-panel/management/edit-shift/{id}";
        }

        if (shift.getEmployee() == null || shift.getEmployee().getId() == 0) {
            redirectAttributes.addAttribute("id", shift.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono pracownika o ID: " + shift.getEmployee().getId());
            return "redirect:/supervisor-panel/management/edit-shift/{id}";
        }

        boolean isNewShiftDayShift = shift.getStartTime().isBefore(shift.getEndTime());
        boolean isOldShiftDayShift = shiftBeforeUpdate.getStartTime().isBefore(shiftBeforeUpdate.getEndTime());

        if(isNewShiftDayShift != isOldShiftDayShift){
            redirectAttributes.addAttribute("id", shift.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Brak możliwości edycji zmiany nocnej na dzienną i odwrotnie");
            return "redirect:/supervisor-panel/management/edit-shift/{id}";
        }

        shift.setWorkDate(shiftBeforeUpdate.getWorkDate());
        shift.setShiftName(shift.getShiftNameByShiftStartTime(shift.getStartTime()));
        shift.setActive(shiftBeforeUpdate.isActive());

        boolean shiftIsActive = isShiftIsActive(shiftBeforeUpdate);

        Employee oldEmployee = shiftBeforeUpdate.getEmployee();
        Employee newEmployee = employeeService.findEmployeeById(shift.getEmployee().getId());
        if(newEmployee.getId() != oldEmployee.getId()){
            if(shiftIsActive){
                redirectAttributes.addAttribute("id", shift.getId());
                redirectAttributes.addFlashAttribute("errorMessage", "Brak możliwości edycji pracownika gdy zmiana jest aktywna");
                return "redirect:/supervisor-panel/management/edit-shift/{id}";
            }
            boolean alreadyHaveShiftThatDay = shiftService.doesEmployeeAlreadyHaveShiftInProvidedDay(shift.getWorkDate(), newEmployee);
            if(alreadyHaveShiftThatDay){
                redirectAttributes.addAttribute("id", shift.getId());
                redirectAttributes.addAttribute("errorMessage", "Pracownik posiada już przypisaną zmianę w ten dzień");
                return "redirect:/supervisor-panel/management/edit-shift/{id}";
            }
            oldEmployee.getShifts().remove(shiftBeforeUpdate);
            employeeService.updateEmployee(oldEmployee);

            newEmployee.getShifts().add(shift);
            shift.setEmployee(newEmployee);
        }
        else{
            if(shiftIsActive){
                if(shift.getStartTime().isAfter(shiftBeforeUpdate.getStartTime())){
                    oldEmployee.setPosition(null);
                    List<PositionEmployeeHistory> histories = historyService.findTodaysActivityByEmployeeId(oldEmployee.getId());
                    if(!histories.isEmpty()){
                        for(PositionEmployeeHistory history:histories){
                            historyService.deleteHistoryById(history.getId());
                        }
                    }
                    employeeService.updateEmployee(oldEmployee);
                }
                if(shift.getEndTime().isBefore(shiftBeforeUpdate.getEndTime())
                        && shift.getEndTime().isBefore(LocalTime.now())){
                    oldEmployee.setPosition(null);
                    PositionEmployeeHistory history = historyService.findByEmployeeIdAndIsActiveTrue(oldEmployee.getId());
                    if(history != null){
                        history.setActive(false);
                        history.setEndTime(LocalTime.now());
                        historyService.updateHistory(history);
                    }
                    employeeService.updateEmployee(oldEmployee);
                    shift.setActive(false);
                }
            }

            shift.setEmployee(oldEmployee);
        }

        shiftService.updateShift(shift);

        return "redirect:/supervisor-panel/management";
    }

    private static boolean isShiftIsActive(Shift shiftBeforeUpdate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime shiftStart = LocalDateTime.of(shiftBeforeUpdate.getWorkDate(), shiftBeforeUpdate.getStartTime());
        LocalDateTime shiftEnd;
        if(shiftBeforeUpdate.getStartTime().isBefore(shiftBeforeUpdate.getEndTime())){
            shiftEnd = LocalDateTime.of(shiftBeforeUpdate.getWorkDate().plusDays(1), shiftBeforeUpdate.getEndTime());
        }
        else{
            shiftEnd = LocalDateTime.of(shiftBeforeUpdate.getWorkDate(), shiftBeforeUpdate.getEndTime());
        }

        return now.isAfter(shiftStart) && now.isBefore(shiftEnd);
    }

}
