package employee.management.system.repository;

import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionEmployeeHistoryRepository extends JpaRepository<PositionEmployeeHistory, Integer> {
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
}
