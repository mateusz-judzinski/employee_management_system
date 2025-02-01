package employee.management.system.service;

import employee.management.system.entity.EmployeeSkill;
import employee.management.system.repository.EmployeeSkillRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeSkillServiceImpl implements EmployeeSkillService{
    private final EmployeeSkillRepository employeeSkillRepository;
    @Autowired
    public EmployeeSkillServiceImpl(EmployeeSkillRepository employeeSkillRepository) {
        this.employeeSkillRepository = employeeSkillRepository;
    }

    @Override
    public void addEmployeeSkill(EmployeeSkill employeeSkill) {
        employeeSkillRepository.save(employeeSkill);
    }

    @Override
    public void saveAll(List<EmployeeSkill> skills) {
        employeeSkillRepository.saveAll(skills);
    }

    @Override
    public EmployeeSkill findEmployeeSkillById(int id) {
        return employeeSkillRepository.findById(id).orElseThrow(() ->
                new RuntimeException("EmployeeSkill with id: " + id + " not found"));
    }

    @Override
    public List<EmployeeSkill> findAllEmployeeSkills() {
        return employeeSkillRepository.findAll();
    }
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    @Override
    public void updateSkillLevel() {
        List<EmployeeSkill> skills = employeeSkillRepository.findAll();
        for (EmployeeSkill employeeSkill:skills) {

            int timeExperienceInMinutes = employeeSkill.getTimeExperienceInMinutes();
            int newProficiencyLevel = calculateSkillProficiency(timeExperienceInMinutes);

            if(employeeSkill.getProficiencyLevel() != newProficiencyLevel){
                employeeSkill.setProficiencyLevel(newProficiencyLevel);
                employeeSkillRepository.save(employeeSkill);
            }
        }
    }

    @Override
    public List<EmployeeSkill> findAllEmployeeSkillByEmployeeId(int employeeId) {
        List<EmployeeSkill> skills = employeeSkillRepository.findAllEmployeeSkillByEmployeeId(employeeId);
        skills.sort(Comparator.comparingInt(EmployeeSkill::getTimeExperienceInMinutes).reversed());

        return skills;
    }

    private int calculateSkillProficiency(int timeExperienceInMinutes){
        if(timeExperienceInMinutes >= 9000){
            return 3;
        } else if (timeExperienceInMinutes >= 3600){
            return 2;
        } else if (timeExperienceInMinutes >= 1200){
            return 1;
        } else {
            return 0;
        }
    }
}
