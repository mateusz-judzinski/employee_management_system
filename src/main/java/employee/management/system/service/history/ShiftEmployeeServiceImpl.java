package employee.management.system.service.history;

import employee.management.system.entity.history.ShiftEmployee;
import employee.management.system.repository.history.ShiftEmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShiftEmployeeServiceImpl implements ShiftEmployeeService {
    private final ShiftEmployeeRepository shiftEmployeeRepository;

    @Autowired
    public ShiftEmployeeServiceImpl(ShiftEmployeeRepository shiftEmployeeRepository) {
        this.shiftEmployeeRepository = shiftEmployeeRepository;
    }

    @Override
    public List<ShiftEmployee> findByEmployeeId(int employeeId) {
        return shiftEmployeeRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<ShiftEmployee> getHistoryForMonth(int month, int year) {
        return shiftEmployeeRepository.getHistoryForMonth(month, year);
    }

    @Override
    public List<ShiftEmployee> getHistoryForDay(int day, int month, int year) {
        return shiftEmployeeRepository.getHistoryForDay(day, month, year);
    }

    @Override
    public List<ShiftEmployee> getAllHistories() {
        return shiftEmployeeRepository.findAll();
    }

    @Override
    public ShiftEmployee findById(int id) {
        return shiftEmployeeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("History with id: " + id + " not found"));
    }

    @Transactional
    @Override
    public void addHistory(ShiftEmployee history) {
        shiftEmployeeRepository.save(history);
    }

    @Transactional
    @Override
    public void updateHistory(ShiftEmployee history) {
        shiftEmployeeRepository.save(history);
    }

    @Transactional
    @Override
    public void deleteHistoryById(int id) {
        if(!shiftEmployeeRepository.existsById(id)){
            throw new RuntimeException("History with id: " + id + " not found");
        }
        shiftEmployeeRepository.deleteById(id);
    }
}
