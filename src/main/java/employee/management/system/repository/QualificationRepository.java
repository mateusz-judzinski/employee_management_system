package employee.management.system.repository;

import employee.management.system.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface QualificationRepository extends JpaRepository<Qualification, Integer> {

    Qualification findQualificationByName(String name);
    boolean existsByName(String name);
}
