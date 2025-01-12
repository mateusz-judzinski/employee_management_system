package employee.management.system.repository;

import employee.management.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE s.id = :skillId")
    List<Employee> findEmployeesBySkillId(int skillId);
    Employee findEmployeeByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
