package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Position;
import employee.management.system.entity.Qualification;
import employee.management.system.entity.Skill;
import employee.management.system.service.PositionService;
import employee.management.system.service.QualificationService;
import employee.management.system.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/management")
public class PositionManagementController {

    private final SkillService skillService;
    private final QualificationService qualificationService;
    private final PositionService positionService;

    public PositionManagementController(SkillService skillService, QualificationService qualificationService, PositionService positionService) {
        this.skillService = skillService;
        this.qualificationService = qualificationService;
        this.positionService = positionService;
    }

    @GetMapping("/add-position")
    public String showPositionForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        Position position = new Position();
        model.addAttribute("position", position);

        List<Skill> skill = skillService.findAllSkills();
        model.addAttribute("skills", skill);

        List<Qualification> qualifications = qualificationService.findAllQualifications();
        Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
        qualifications.remove(noIdCardQualification);
        model.addAttribute("qualifications", qualifications);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/positions/form";
    }

    @PostMapping("/add-position")
    public String addPosition(@ModelAttribute("position") Position position,
                              RedirectAttributes redirectAttributes) {

        boolean positionExists = positionService.existsByPositionName(position.getPositionName());

        if (positionExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stanowisko o podanej nazwie już istnieje.");
            return "redirect:/supervisor-panel/management/add-position";
        }

        if(position.getSkill() != null){
            Skill skill = skillService.findSkillById(position.getSkill().getId());
            position.setSkill(skill);
        }

        List<Qualification> selectedQualifications = new ArrayList<>();

        if(position.getNeededQualifications().isEmpty() || position.getNeededQualifications() == null){
            Qualification noIdCardQualification = qualificationService.findQualificationByName("brak stałej przepustki");
            selectedQualifications.add(noIdCardQualification);
            position.setNeededQualifications(selectedQualifications);
        }

        else{
            List<Integer> qualificationIds = new ArrayList<>();
            for (Qualification qualification : position.getNeededQualifications()) {
                qualificationIds.add(qualification.getId());
            }

            for (Integer id:qualificationIds){
                Qualification nextQualification = qualificationService.findQualificationById(id);
                selectedQualifications.add(nextQualification);
            }

            position.setNeededQualifications(selectedQualifications);
        }

        position.setIsActive(false);
        position.setTemporary(false);

        positionService.addPosition(position);

        return "redirect:/supervisor-panel/management";
    }


}
