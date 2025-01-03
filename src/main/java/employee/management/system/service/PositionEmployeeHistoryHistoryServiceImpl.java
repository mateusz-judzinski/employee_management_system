package employee.management.system.service;

import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionEmployeeHistoryHistoryServiceImpl implements PositionEmployeeHistoryService {
    private final PositionEmployeeHistoryRepository positionEmployeeHistoryRepository;

    @Autowired
    public PositionEmployeeHistoryHistoryServiceImpl(PositionEmployeeHistoryRepository positionEmployeeHistoryRepository) {
        this.positionEmployeeHistoryRepository = positionEmployeeHistoryRepository;
    }

    @Override
    public List<PositionEmployeeHistory> findByEmployeeId(int employeeId) {
        return positionEmployeeHistoryRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<PositionEmployeeHistory> getAllHistories() {
        return positionEmployeeHistoryRepository.findAll();
    }

    @Override
    public PositionEmployeeHistory findById(int id) {
        return positionEmployeeHistoryRepository.findById(id).orElseThrow(() ->
                new RuntimeException("History with id: " + id + " not found"));
    }

    @Transactional
    @Override
    public void addHistory(PositionEmployeeHistory history) {
        positionEmployeeHistoryRepository.save(history);
    }

    @Transactional
    @Override
    public void updateHistory(PositionEmployeeHistory history) {
        positionEmployeeHistoryRepository.save(history);
    }

    @Transactional
    @Override
    public void deleteHistoryById(int id) {
        if(!positionEmployeeHistoryRepository.existsById(id)){
            throw new RuntimeException("History with id: " + id + " not found");
        }
        positionEmployeeHistoryRepository.deleteById(id);
    }
}
