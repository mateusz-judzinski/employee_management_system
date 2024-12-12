package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Skill;
import employee.management.system.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService{
    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill findSkillById(int id) {
        return skillRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Skill with id: " + id + " not found"));
    }

    @Override
    public Skill findSkillBySkillName(String skillName) {
        return skillRepository.findSkillBySkillName(skillName);
    }

    @Override
    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public void addSkill(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public void updateSkill(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public void deleteSkillById(int id) {
        skillRepository.deleteById(id);
    }

    @Override
    public List<Skill> findSkillsByEmployeeId(int employeeId) {
        return skillRepository.findSkillsByEmployeeId(employeeId);
    }
}
