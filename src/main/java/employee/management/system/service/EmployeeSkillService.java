package employee.management.system.service;

import employee.management.system.entity.EmployeeSkill;

import java.util.List;

public interface EmployeeSkillService {

    void saveAll(List<EmployeeSkill> skills);
    EmployeeSkill findEmployeeSkillById(int id);
    List<EmployeeSkill> findAllEmployeeSkills();
    void updateSkillLevel();
    List<EmployeeSkill> findAllEmployeeSkillByEmployeeId(int employeeId);
}
