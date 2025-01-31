package employee.management.system.service;

import employee.management.system.entity.Employee;
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
    List<Shift> getEntireDayScheduleByWorkDate(LocalDate selectedDate);
    void importSchedule(MultipartFile file) throws IOException;
    void updateEmployeesWithCurrentShift();
    List<List<Shift>> splitOnActiveAndInactive(List<Shift> shifts);
    List<Shift> getMonthShiftForEmployeeById(int employeeId, int month, int year);
    boolean doesEmployeeAlreadyHaveShiftInProvidedDay(LocalDate localDate, Employee employee);
    void saveAll (List<Shift> shifts);
    List<Shift> getShiftsForMonthAndYear(int month, int year);
}
