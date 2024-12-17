package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.Skill;
import employee.management.system.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/skills")
public class SkillManagementController {

    private final SkillService skillService;

    @Autowired
    public SkillManagementController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public String getAllSkills(Model model) {
        List<Skill> skills = skillService.findAllSkills();
        model.addAttribute("skills", skills);
        return "supervisor/skill/list";
    }

    @GetMapping("/new")
    public String showAddSkillForm(Model model) {
        model.addAttribute("skill", new Skill());
        return "supervisor/skill/form";
    }

    @PostMapping
    public String saveSkill(@ModelAttribute("skill") Skill skill) {
        skillService.addSkill(skill);
        return "redirect:/supervisor-panel/skills";
    }

    @GetMapping("/edit/{skillId}")
    public String showEditSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillService.findSkillById(skillId);
        model.addAttribute("skill", skill);
        return "supervisor/skill/edit";
    }

    @PostMapping("/update")
    public String updateSkill(@ModelAttribute("skill") Skill skill) {
        skillService.updateSkill(skill);
        return "redirect:/supervisor-panel/skills";
    }

    @GetMapping("/delete/{skillId}")
    public String deleteSkill(@PathVariable("skillId") int skillId) {
        skillService.deleteSkillById(skillId);
        return "redirect:/supervisor-panel/skills";
    }
}
