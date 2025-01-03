package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.PositionEmployeeHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PositionEmployeeHistoryService {
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
    List<PositionEmployeeHistory> getAllHistories();
    PositionEmployeeHistory findById(int id);
    void addHistory(PositionEmployeeHistory history);
    void updateHistory(PositionEmployeeHistory history);
    void deleteHistoryById(int id);
    Map<Integer, LocalDateTime> findAllActiveEmployeesAndStartTimeDate();
}
