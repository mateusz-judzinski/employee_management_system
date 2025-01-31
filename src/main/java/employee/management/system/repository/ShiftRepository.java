package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Shift;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findShiftsByWorkDate(LocalDate workDate);
    @Query("SELECT s FROM Shift s WHERE " +
            "s.workDate = :day OR (s.workDate = :dayBefore AND s.startTime > s.endTime) " +
            "ORDER BY s.workDate ASC, s.startTime ASC")
    List<Shift> findEntireDaySchedule(@Param("day") LocalDate day, @Param("dayBefore") LocalDate dayBefore);
    @Query("SELECT COUNT(s) > 0 FROM Shift s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
    boolean existsByMonthAndYear(@Param("month") int month, @Param("year")int year);
    @Query("SELECT s FROM Shift s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
    List<Shift> getShiftsByMonthAndYear(@Param("month") int month, @Param("year") int year);
    @Query("SELECT s FROM Shift s WHERE " +
            "((s.workDate = :today AND s.startTime <= CURRENT_TIME AND " +
            "(s.endTime > CURRENT_TIME OR s.endTime < s.startTime)) " +
            "OR (s.workDate = :yesterday AND s.startTime > s.endTime AND s.endTime > CURRENT_TIME))")
    List<Shift> findCurrentShifts(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday);
    @Query("SELECT s FROM Shift s " +
            "WHERE s.isActive = true " +
            "AND ((s.workDate < :yesterday) " +
            "OR (s.workDate = :yesterday AND s.startTime < s.endTime) " +
            "OR (s.workDate = :yesterday AND s.startTime > s.endTime AND CURRENT_TIME > s.endTime) " +
            "OR (s.workDate = :today AND s.startTime < s.endTime AND CURRENT_TIME > s.endTime))")
    List<Shift> findFinishedShiftsWithActiveOnTrue(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday);
    @Query("SELECT s FROM Shift s WHERE s.employee.id = :employeeId AND MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year ORDER BY s.workDate ASC")
    List<Shift> getMonthScheduleForEmployeeById(@Param("employeeId") int employeeId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(s) > 0 FROM Shift s WHERE s.employee = :employee AND s.workDate = :localDate")
    boolean doesEmployeeHaveShiftInProvidedDay(LocalDate localDate, Employee employee);

    
}
