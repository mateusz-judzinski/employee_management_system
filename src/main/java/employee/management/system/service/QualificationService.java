package employee.management.system.service;

import employee.management.system.entity.Qualification;

import java.util.List;

public interface QualificationService {

    Qualification findQualificationByName(String name);
    Qualification findQualificationById(int id);
    List<Qualification> findAllQualifications();
    void addQualification(Qualification qualification);
    void updateQualification(Qualification qualification);
    void deleteQualificationById(int id);
}
