package employee.management.system.service;

import employee.management.system.dto.AveragePositionTime;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import employee.management.system.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionEmployeeHistoryServiceImpl implements PositionEmployeeHistoryService {
    private final PositionEmployeeHistoryRepository positionEmployeeHistoryRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public PositionEmployeeHistoryServiceImpl(PositionEmployeeHistoryRepository positionEmployeeHistoryRepository, PositionRepository positionRepository) {
        this.positionEmployeeHistoryRepository = positionEmployeeHistoryRepository;
        this.positionRepository = positionRepository;
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

    @Override
    public LocalTime getTimeSpentOnPositionByEmployeeAndPositionIds(int employeeId, int positionId) {
        List<PositionEmployeeHistory> histories = positionEmployeeHistoryRepository.findAllPositionEmployeeHistoryByEmployeeIdAndPositionId(employeeId, positionId);
        LocalTime totalTime = LocalTime.of(0, 0, 0);

        for (PositionEmployeeHistory history : histories) {
            Duration duration;
            LocalTime startTime = history.getStartTime();
            LocalTime endTime = history.getEndTime();

            if (endTime.isBefore(startTime)) {
                duration = Duration.between(startTime, endTime.plusHours(24));
            } else {
                duration = Duration.between(startTime, endTime);
            }

            totalTime = totalTime.plusSeconds(duration.getSeconds());
        }
        return totalTime;
    }

    @Override
    public List<AveragePositionTime> calculateEmployeePositionRatioPercentage(int employeeId) {
        long allPositionsSumInSeconds = 0;
        List<Long> allPositionsDurationsInSeconds = new ArrayList<>();
        List<AveragePositionTime> averagePositionTimes = new ArrayList<>();

        List<Position> allPositions = positionRepository.findAllPositionsForCalculations();

        for (Position position : allPositions) {
            LocalTime temp = getTimeSpentOnPositionByEmployeeAndPositionIds(employeeId, position.getId());
            long durationInSeconds = temp.toSecondOfDay();
            allPositionsSumInSeconds += durationInSeconds;
            allPositionsDurationsInSeconds.add(durationInSeconds);
        }

        for (int i = 0; i < allPositions.size(); i++) {
            Position position = allPositions.get(i);
            long durationInSeconds = allPositionsDurationsInSeconds.get(i);

            double percentage = allPositionsSumInSeconds > 0 ? (double) durationInSeconds / allPositionsSumInSeconds * 100 : 0;
            percentage = Math.round(percentage * 100.0) / 100.0;

            String formattedTime = String.format("%02d:%02d:%02d",
                    durationInSeconds / 3600,
                    (durationInSeconds % 3600) / 60,
                    durationInSeconds % 60);

            averagePositionTimes.add(new AveragePositionTime(position.getPositionName(), formattedTime, percentage));
        }

        return averagePositionTimes;
    }



}
