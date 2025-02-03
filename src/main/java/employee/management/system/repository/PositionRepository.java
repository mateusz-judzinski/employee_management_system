package employee.management.system.repository;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Position findPositionByPositionName(String name);
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.isTemporary = FALSE")
    List<Position> findAllPositionsForCalculations();
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.positionName != 'inne' AND p.isTemporary = FALSE")
    List<Position> getPositionForManagement();
    boolean existsByPositionName(String positionName);
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.positionName != 'inne' AND p.isTemporary = FALSE AND p.skill IS null")
    List<Position> getPositionForAddForm();
    @Query("SELECT p FROM Position p WHERE p.positionName != 'przerwa' AND p.positionName != 'inne' AND p.isTemporary = FALSE AND (p.skill IS null OR p.skill.id = :skillId)")
    List<Position> getPositionForEditForm(int skillId);
    List<Position> findPositionBySkillId(int skillId);
    @Query("SELECT p FROM Position p JOIN p.neededQualifications nq WHERE nq.id = :qualificationId")
    List<Position> findPositionByQualificationId(int qualificationId);
}
