package employee.management.system.repository;

import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PositionEmployeeHistoryRepository extends JpaRepository<PositionEmployeeHistory, Integer> {
    PositionEmployeeHistory findByEmployeeIdAndIsActiveTrue(int employeeId);
    @Query("SELECT history FROM PositionEmployeeHistory history WHERE history.isActive = true")
    List<PositionEmployeeHistory> findActivePositionEmployeeHistories();
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
}
