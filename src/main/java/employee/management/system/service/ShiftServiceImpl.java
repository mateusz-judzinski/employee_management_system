package employee.management.system.service;

import employee.management.system.entity.Shift;
import employee.management.system.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift findShiftById(int id) {
        return shiftRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Shift with id: " + id + " not found"));
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    @Transactional
    @Override
    public void addShift(Shift shift) {
        shiftRepository.save(shift);
    }

    @Transactional
    @Override
    public void updateShift(Shift shift) {
        shiftRepository.save(shift);
    }

    @Transactional
    @Override
    public void deleteShiftById(int id) {
        shiftRepository.deleteById(id);
    }

    @Override
    public List<Shift> findShiftsByWorkDate(LocalDate workDate) {
        return shiftRepository.findShiftsByWorkDate(workDate);
    }
}
