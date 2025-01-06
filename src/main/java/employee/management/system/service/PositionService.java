package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionService {
    Position findPositionById(int id);
    List<Position> findAllPositions();
    void addPosition(Position position);
    void updatePosition(Position position);
    void deletePositionById(int id);
    Position findPositionByName(String name);
    void switchActiveStatusByPositionId(int positionId);
    void addEmployeesIntoPosition(int positionId, List<Integer> employeesIds);
    void removeEmployeeFromPositionByEmployeeId(int employeeId);
    void addTemporaryPosition(String positionName, String description);
}
