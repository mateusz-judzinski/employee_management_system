package employee.management.system.repository;

import employee.management.system.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface QualificationRepository extends JpaRepository<Qualification, Integer> {

    Qualification findQualificationByName(String name);
    boolean existsByName(String name);
    @Query("SELECT q FROM Qualification q WHERE q.name != 'stała przepustka' AND q.name != 'brak stałej przepustki'")
    List<Qualification> getQualificationsForManagement();
}
