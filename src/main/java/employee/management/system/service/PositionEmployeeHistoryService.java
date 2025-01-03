package employee.management.system.service;

import employee.management.system.entity.PositionEmployeeHistory;

import java.util.List;

public interface PositionEmployeeHistoryService {
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
    List<PositionEmployeeHistory> getAllHistories();
    PositionEmployeeHistory findById(int id);
    void addHistory(PositionEmployeeHistory history);
    void updateHistory(PositionEmployeeHistory history);
    void deleteHistoryById(int id);
}
