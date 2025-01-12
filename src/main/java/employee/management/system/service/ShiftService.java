package employee.management.system.service;

import employee.management.system.entity.Shift;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ShiftService {
    Shift findShiftById(int id);
    List<Shift> getAllShifts();
    void addShift(Shift shift);
    void updateShift(Shift shift);
    void deleteShiftById(int id);
    List<Shift> findShiftsByWorkDate(LocalDate workDate);
    Map<String, List<String>> getScheduleForMonth(Integer month);
    List<Shift> getScheduleForDay(Integer day);
    List<Shift> getShiftsByWorkDate(LocalDate selectedDate);
    void importSchedule(MultipartFile file) throws IOException;
}
