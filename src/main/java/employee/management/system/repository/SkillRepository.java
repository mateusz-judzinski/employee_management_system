package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

}
