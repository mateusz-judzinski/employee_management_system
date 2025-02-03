package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.formula.functions.Poisson;
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
    void processHistory(Employee employee, Position newPosition);
    List<Position> getPositionsForManagement();
    boolean existsByPositionName(String positionName);
    List<Position> getPositionForAddForm();
    List<Position> getPositionsByIds(List<Integer> ids);
    List<Position> getPositionForEditForm(int skillId);
    List<Position> findPositionBySkillId(int skillId);
    List<Position> findPositionByQualificationId(int qualificationId);
    boolean isPositionNameOccupied(Position position);
}
