package employee.management.system.repository;

import employee.management.system.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    @Query("SELECT s FROM Shift s WHERE MONTH(s.workDate) = :month ORDER BY s.workDate ASC")
    List<Shift> getScheduleForMonth(int month);
    @Query("SELECT s FROM Shift s WHERE DAY(s.workDate) = :day AND MONTH(s.workDate) = :month ORDER BY s.startTime ASC")
    List<Shift> getScheduleForDay(int day, int month);
    List<Shift> findShiftsByWorkDate(LocalDate workDate);
}
