package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.service.PositionEmployeeHistoryService;
import employee.management.system.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/positions-history")
public class PositionEmployeeHistoryController {

    private final PositionEmployeeHistoryService historyService;

    @Autowired
    public PositionEmployeeHistoryController(PositionEmployeeHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public String getAllPositionEmployeeHistory(Model model) {
        List<PositionEmployeeHistory> histories = historyService.getAllHistories();
        model.addAttribute("histories", histories);
        return "supervisor/position_employee_history/panel";
    }
}
