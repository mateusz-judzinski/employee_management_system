package employee.management.system.controller.supervisor.management;

import employee.management.system.service.ShiftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/supervisor-panel/management")
public class ShiftManagementController {
    private final ShiftService shiftService;

    public ShiftManagementController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/import-schedule")
    public String importSchedulePage(){
        return "supervisor/shifts/import-schedule";
    }

    @PostMapping("/import-schedule")
    public String importSchedule(@RequestParam("file") MultipartFile file, Model model){
        try{
            shiftService.importSchedule(file);
        } catch (IOException e) {
            String error = "An error occurred: " + e.getMessage();
            model.addAttribute("error", error);
            return "user/error";
        }
        return "redirect:/supervisor-panel/management/import";
    }

    @GetMapping("/add-shift")
    public String showShiftForm(){

        return "supervisor/shifts/form";
    }
}
