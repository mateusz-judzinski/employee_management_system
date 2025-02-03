package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE s.id = :skillId")
    List<Employee> findEmployeesBySkillId(int skillId);
    Employee findEmployeeByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    List<Employee> findByPositionIsNotNull();
    List<Employee> findAllByOrderByLastNameAsc();
    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.position IS NOT NULL
              AND NOT EXISTS (
                  SELECT s
                  FROM Shift s
                  WHERE s.employee = e
                    AND (
                        (s.workDate = :today
                         AND s.startTime < s.endTime
                         AND CURRENT_TIME BETWEEN s.startTime AND s.endTime)
                        OR
                        (s.workDate = :today
                         AND s.startTime > s.endTime
                         AND CURRENT_TIME >= s.startTime)
                        OR
                        (s.workDate = :yesterday
                         AND s.startTime > s.endTime
                         AND CURRENT_TIME <= s.endTime)
                    )
              )
            """)
    List<Employee> findEmployeesThatFinishedShift(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday);
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Employee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    @Query("SELECT e FROM Employee e JOIN e.shifts s WHERE " +
            "((s.workDate = :today AND s.startTime <= CURRENT_TIME AND " +
            "(s.endTime > CURRENT_TIME OR s.endTime < s.startTime)) " +
            "OR (s.workDate = :yesterday AND s.startTime > s.endTime AND s.endTime > CURRENT_TIME))")
    List<Employee> findEmployeesWithCurrentShifts(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday);
    boolean existsByIdCardNumber(Integer idCardNumber);
    @Query("SELECT e FROM Employee e JOIN e.qualifications q WHERE q.id = :qualificationId")
    List<Employee> findEmployeesByQualificationId(int qualificationId);
    Employee findEmployeeByIdCardNumber(Integer idCardNumber);
}
