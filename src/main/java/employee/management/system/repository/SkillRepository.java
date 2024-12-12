package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query("SELECT s FROM Skill s JOIN s.employees e WHERE e.id = :employeeId")
    List<Skill> findSkillsByEmployeeId(int employeeId);
    Skill findSkillBySkillName(String skillName);
}
