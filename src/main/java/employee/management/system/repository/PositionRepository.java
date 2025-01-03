package employee.management.system.repository;

import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Position findPositionByPositionName(String name);
}
