package employee.management.system.service;

import employee.management.system.dto.AveragePositionTime;
import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface PositionEmployeeHistoryService {
    PositionEmployeeHistory findByEmployeeIdAndIsActiveTrue(int employeeId);
    List<PositionEmployeeHistory> findByEmployeeId(int employeeId);
    List<PositionEmployeeHistory> getAllHistories();
    PositionEmployeeHistory findById(int id);
    void addHistory(PositionEmployeeHistory history);
    void updateHistory(PositionEmployeeHistory history);
    void deleteHistoryById(int id);
    Map<Integer, LocalDateTime> findAllActiveEmployeesAndStartTimeDate();
    LocalTime getTimeSpentOnPositionByEmployeeAndPositionIds(int employeeId, int positionId);
    List<AveragePositionTime> calculateEmployeePositionRatioPercentage(int employeeId);
    List<PositionEmployeeHistory> findTodaysActivityByEmployeeId(int employeeId);
    List<PositionEmployeeHistory> findAllByPositionId(int positionId);
}
