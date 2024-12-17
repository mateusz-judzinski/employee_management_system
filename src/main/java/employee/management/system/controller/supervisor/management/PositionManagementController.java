package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Position;
import employee.management.system.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/positions")
public class PositionManagementController {

    private final PositionService positionService;

    @Autowired
    public PositionManagementController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public String getAllPositions(Model model) {
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("positions", positions);
        return "supervisor/position/list";
    }

    @GetMapping("/new")
    public String showAddPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "supervisor/position/form";
    }

    @PostMapping
    public String savePosition(@ModelAttribute("position") Position position) {
        positionService.addPosition(position);
        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/edit/{positionId}")
    public String showEditPositionForm(@PathVariable("positionId") int positionId, Model model) {
        Position position = positionService.findPositionById(positionId);
        model.addAttribute("position", position);
        return "supervisor/position/edit";
    }

    @PostMapping("/update")
    public String updatePosition(@ModelAttribute("position") Position position) {
        positionService.updatePosition(position);
        return "redirect:/supervisor-panel/positions";
    }

    @GetMapping("/delete/{positionId}")
    public String deletePosition(@PathVariable("positionId") int positionId) {
        positionService.deletePositionById(positionId);
        return "redirect:/supervisor-panel/positions";
    }
}
