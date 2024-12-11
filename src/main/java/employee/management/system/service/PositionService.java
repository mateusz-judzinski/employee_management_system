package employee.management.system.service;

import employee.management.system.entity.Position;

import java.util.List;

public interface PositionService {

    Position findPositionById(int id);
    List<Position> findAllPositions();
    void addPosition(Position position);
    void updatePosition(Position position);
    void deletePositionById(int id);
    Position findPositionByName(String name);
}
