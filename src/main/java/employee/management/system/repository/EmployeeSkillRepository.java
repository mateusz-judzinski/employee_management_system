package employee.management.system.repository;

import employee.management.system.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Integer> {
    EmployeeSkill findEmployeeSkillByEmployeeIdAndSkillId(int employeeId, int skillId);
    List<EmployeeSkill> findAllEmployeeSkillByEmployeeId(int employeeId);
    List<EmployeeSkill> findAllEmployeeSkillBySkillId(int skillId);
}
