package employee.management.system.repository;

import employee.management.system.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    Position findPositionByPositionName(String name);
}
