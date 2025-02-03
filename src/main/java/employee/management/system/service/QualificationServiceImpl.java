package employee.management.system.service;

import employee.management.system.entity.Qualification;
import employee.management.system.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;

    public QualificationServiceImpl(QualificationRepository qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    @Override
    public Qualification findQualificationByName(String name) {
        return qualificationRepository.findQualificationByName(name);
    }

    @Override
    public Qualification findQualificationById(int id) {
        return qualificationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Qualification with id: " + id + " not found"));
    }

    @Override
    public List<Qualification> findAllQualifications() {
        return qualificationRepository.findAll();
    }

    @Override
    public void addQualification(Qualification qualification) {
        qualificationRepository.save(qualification);
    }

    @Override
    public void updateQualification(Qualification qualification) {
        qualificationRepository.save(qualification);
    }

    @Override
    public void deleteQualificationById(int id) {
        qualificationRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return qualificationRepository.existsByName(name);
    }

    @Override
    public List<Qualification> getQualificationsForManagement() {
        return qualificationRepository.getQualificationsForManagement();
    }

    @Override
    public boolean isQualificationNameOccupied(Qualification qualification) {
        Qualification nameOwner = qualificationRepository.findQualificationByName(qualification.getName());

        if (nameOwner == null) {
            return false;
        } else {
            return nameOwner.getId() != qualification.getId();
        }
    }
}
