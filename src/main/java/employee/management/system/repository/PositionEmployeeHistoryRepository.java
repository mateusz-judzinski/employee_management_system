package employee.management.system.repository;

import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PositionEmployeeHistoryRepository extends JpaRepository<PositionEmployeeHistory, Integer> {
    PositionEmployeeHistory findByEmployeeIdAndIsActiveTrue(int employeeId);
    @Query("SELECT history FROM PositionEmployeeHistory history WHERE history.isActive = true")
    List<PositionEmployeeHistory> findActivePositionEmployeeHistories();
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
    @Query("SELECT history FROM PositionEmployeeHistory history JOIN history.position position WHERE position.positionName = :positionName")
    List<PositionEmployeeHistory> findAllByPositionName(@Param("positionName") String positionName);
    List<PositionEmployeeHistory> findAllPositionEmployeeHistoryByEmployeeIdAndPositionId(int employeeId, int positionId);

}
