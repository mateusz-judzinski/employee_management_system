package employee.management.system.service.history;

import employee.management.system.entity.history.PositionEmployee;
import employee.management.system.repository.history.PositionEmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionEmployeeServiceImpl implements PositionEmployeeService{
    private final PositionEmployeeRepository positionEmployeeRepository;

    @Autowired
    public PositionEmployeeServiceImpl(PositionEmployeeRepository positionEmployeeRepository) {
        this.positionEmployeeRepository = positionEmployeeRepository;
    }

    @Override
    public List<PositionEmployee> findByEmployeeId(int employeeId) {
        return positionEmployeeRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<PositionEmployee> getAllHistories() {
        return positionEmployeeRepository.findAll();
    }

    @Override
    public PositionEmployee findById(int id) {
        return positionEmployeeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("History with id: " + id + " not found"));
    }

    @Transactional
    @Override
    public void addHistory(PositionEmployee history) {
        positionEmployeeRepository.save(history);
    }

    @Transactional
    @Override
    public void updateHistory(PositionEmployee history) {
        positionEmployeeRepository.save(history);
    }

    @Transactional
    @Override
    public void deleteHistoryById(int id) {
        if(!positionEmployeeRepository.existsById(id)){
            throw new RuntimeException("History with id: " + id + " not found");
        }
        positionEmployeeRepository.deleteById(id);
    }
}
