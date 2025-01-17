package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    @Query("SELECT s FROM Shift s WHERE MONTH(s.workDate) = :month ORDER BY s.workDate ASC")
    List<Shift> getScheduleForMonth(int month);
    @Query("SELECT s FROM Shift s WHERE DAY(s.workDate) = :day AND MONTH(s.workDate) = :month ORDER BY s.startTime ASC")
    List<Shift> getScheduleForDay(int day, int month);
    List<Shift> findShiftsByWorkDate(LocalDate workDate);
    @Query("SELECT COUNT(s) > 0 FROM Shift s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
    boolean existsByMonthAndYear(@Param("month") int month, @Param("year")int year);
    @Query("SELECT s FROM Shift s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
    List<Shift> getShiftsByMonthAndYear(@Param("month") int month, @Param("year") int year);
    @Query("SELECT s FROM Shift s WHERE " +
            "((s.workDate = :today AND s.startTime <= CURRENT_TIME AND " +
            "(s.endTime > CURRENT_TIME OR s.endTime < s.startTime)) " +
            "OR (s.workDate = :yesterday AND s.startTime > s.endTime AND s.endTime > CURRENT_TIME))")
    List<Shift> findCurrentShifts(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday);



}
