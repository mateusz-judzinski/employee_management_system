package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Shift;
import employee.management.system.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/supervisor-panel/schedule")
public class ScheduleManagementController {

    private final ShiftService shiftService;

    @Autowired
    public ScheduleManagementController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/month-schedule")
    public String getMonthlySchedule(@RequestParam(value = "month", required = false) Integer month, Model model) {
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
}
