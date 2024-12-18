package employee.management.system.service.history;

import employee.management.system.entity.history.ShiftEmployee;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftEmployeeService {
    List<ShiftEmployee> findByEmployeeId(int employeeId);
    List<ShiftEmployee> getHistoryForMonth(int month, int year);
    List<ShiftEmployee> getHistoryForDay(int day, int month, int year);
    List<ShiftEmployee> getAllHistories();
    ShiftEmployee findById(int id);
    void addHistory(ShiftEmployee history);
    void updateHistory(ShiftEmployee history);
    void deleteHistoryById(int id);
}
