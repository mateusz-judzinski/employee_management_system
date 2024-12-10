package employee.management.system.repository;

import employee.management.system.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findShiftsByWorkDate(LocalDate workDate);
}
