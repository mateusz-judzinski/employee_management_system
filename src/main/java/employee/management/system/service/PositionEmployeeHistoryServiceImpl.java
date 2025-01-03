package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionEmployeeHistoryServiceImpl implements PositionEmployeeHistoryService {
    private final PositionEmployeeHistoryRepository positionEmployeeHistoryRepository;

    @Autowired
    public PositionEmployeeHistoryServiceImpl(PositionEmployeeHistoryRepository positionEmployeeHistoryRepository) {
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

    @Override
    public Map<Integer, LocalDateTime> findAllActiveEmployeesAndStartTimeDate() {
        List<PositionEmployeeHistory> histories = positionEmployeeHistoryRepository.findActivePositionEmployeeHistories();

        Map<Integer, LocalDateTime> activeEmployees = new HashMap<>();
        for (PositionEmployeeHistory history : histories) {
            int employeeId = history.getEmployee().getId();
            LocalDate startDate = history.getStartDate();
            LocalTime startTime = history.getStartTime();

            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            activeEmployees.put(employeeId, startDateTime);
        }

        return activeEmployees;
    }
}
