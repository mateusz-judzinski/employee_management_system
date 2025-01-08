package employee.management.system.service;

import employee.management.system.entity.Shift;
import employee.management.system.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        if(!shiftRepository.existsById(id)){
            throw new RuntimeException("Shift with id: " + id + " not found");
        }
        shiftRepository.deleteById(id);
    }

    @Override
    public List<Shift> findShiftsByWorkDate(LocalDate workDate) {
        return shiftRepository.findShiftsByWorkDate(workDate);
    }

    @Override
    public Map<String, List<String>> getScheduleForMonth(Integer month) {
        int monthToUse = (month != null) ? month : LocalDate.now().getMonthValue();
        List<Shift> shifts = shiftRepository.getScheduleForMonth(monthToUse);

        return splitMonthScheduleOnDays(shifts);
    }

    @Override
    public List<Shift> getScheduleForDay(Integer day) {
        int dayToUse = (day != null) ? day : LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();

        return shiftRepository.getScheduleForDay(dayToUse, month);
    }

    @Override
    public List<Shift> getShiftsByWorkDate(LocalDate selectedDate) {
        return shiftRepository.findShiftsByWorkDate(selectedDate);
    }

    private Map<String, List<String>> splitMonthScheduleOnDays(List<Shift> shifts){

        Map<String, List<String>> dailySchedule = new TreeMap<>();

        for (Shift shift : shifts) {
            String date = shift.getWorkDate().format(DateTimeFormatter.ofPattern("dd.MM"));
            String employeeName = shift.getEmployee().getLastName() + " " + shift.getEmployee().getFirstName();

            dailySchedule.computeIfAbsent(date, k -> new ArrayList<>()).add(employeeName);
        }

        return dailySchedule;
    }
}
