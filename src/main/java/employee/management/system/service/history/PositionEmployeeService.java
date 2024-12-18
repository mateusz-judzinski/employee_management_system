package employee.management.system.service.history;

import employee.management.system.entity.history.PositionEmployee;

import java.util.List;

public interface PositionEmployeeService {
    List<PositionEmployee> findByEmployeeId(int employeeId);
    List<PositionEmployee> getAllHistories();
    PositionEmployee findById(int id);
    void addHistory(PositionEmployee history);
    void updateHistory(PositionEmployee history);
    void deleteHistoryById(int id);
}
