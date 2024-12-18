package employee.management.system.repository.history;

import employee.management.system.entity.Shift;
import employee.management.system.entity.history.ShiftEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftEmployeeRepository extends JpaRepository<ShiftEmployee, Integer> {
    List<ShiftEmployee> findByEmployeeId(int employeeId);
    @Query("SELECT s FROM ShiftEmployee s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year ORDER BY s.workDate ASC")
    List<ShiftEmployee> getHistoryForMonth(int month, int year);
    @Query("SELECT s FROM ShiftEmployee s WHERE DAY(s.workDate) = :day AND MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year ORDER BY s.startTime ASC")
    List<ShiftEmployee> getHistoryForDay(int day, int month, int year);
}
