package employee.management.system.repository;

import employee.management.system.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Integer> {
    EmployeeSkill findEmployeeSkillByEmployeeIdAndSkillId(int employeeId, int skillId);
}
