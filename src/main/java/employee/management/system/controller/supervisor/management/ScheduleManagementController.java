package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Shift;
import employee.management.system.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/import")
    public String importSchedulePage(){
        return "supervisor/schedule/import-schedule";
    }

    @PostMapping("/import")
    public String importSchedule(@RequestParam("file") MultipartFile file, Model model){
        try{
            shiftService.importSchedule(file);
        } catch (IOException e) {
            String error = "An error occurred: " + e.getMessage();
            model.addAttribute("error", error);
            return "user/error";
        }
        return "redirect:/supervisor-panel/schedule/import";
    }
}
