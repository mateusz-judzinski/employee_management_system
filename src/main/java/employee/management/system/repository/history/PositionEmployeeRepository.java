package employee.management.system.repository.history;

import employee.management.system.entity.Position;
import employee.management.system.entity.history.PositionEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionEmployeeRepository extends JpaRepository<PositionEmployee, Integer> {
    List<PositionEmployee> findByEmployeeId(int employeeId);
}
