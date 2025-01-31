package employee.management.system.repository;

import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Position findPositionByPositionName(String name);
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.isTemporary = FALSE")
    List<Position> findAllPositionsForCalculations();
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.positionName != 'inne' AND p.isTemporary = FALSE")
    List<Position> getPositionForManagement();
    boolean existsByPositionName(String positionName);
}
