package employee.management.system.controller.supervisor.management.history;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor-panel/history")
public class HistoryController {

    @GetMapping()
    public String getHistoriesPanel(){
        return "supervisor/history/panel";
    }
}