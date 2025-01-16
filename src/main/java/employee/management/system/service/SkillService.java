package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Skill;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SkillService {
    Skill findSkillById(int id);
    Skill findSkillBySkillName(String skillName);
    List<Skill> findAllSkills();
    void addSkill(Skill skill);
    void updateSkill(Skill skill);
    void deleteSkillById(int id);
    List<Skill> findSkillsByEmployeeId(int employeeId);
}
