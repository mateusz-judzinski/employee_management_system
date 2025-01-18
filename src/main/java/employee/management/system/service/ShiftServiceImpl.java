package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.entity.Shift;
import employee.management.system.repository.EmployeeRepository;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import employee.management.system.repository.PositionRepository;
import employee.management.system.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final PositionEmployeeHistoryRepository historyRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, PositionRepository positionRepository, PositionEmployeeHistoryRepository historyRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.historyRepository = historyRepository;
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
    public List<Shift> getEntireDayScheduleByWorkDate(LocalDate selectedDate) {
        LocalDate dayBefore = selectedDate.minusDays(1);
        return shiftRepository.findEntireDaySchedule(selectedDate, dayBefore);
    }

    @Override
    @Transactional
    public void importSchedule(MultipartFile file) throws IOException {

        int month = LocalDate.now().getMonthValue() + 1;
        int year = LocalDate.now().getYear();

        deleteScheduleIfExists(month, year);

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        int continueRowIndex = 1;
        int continueColumnIndex = 1;
        Row continueCheckRow = sheet.getRow(continueRowIndex);

        int employeeNameRowIndex = 1;
        int employeeNameColumnIndex = 3;
        Cell employeeNameCell = sheet.getRow(employeeNameRowIndex).getCell(employeeNameColumnIndex);
        String employeeNameStringValue = employeeNameCell.getStringCellValue();

        int shiftStartRowIndex = 6;
        int shiftStartColumnIndex = 3;
        Cell shiftStartCell = sheet.getRow(shiftStartRowIndex).getCell(shiftStartColumnIndex);

        int shiftEndRowIndex = 6;
        int shiftEndColumnIndex = 4;
        Cell shiftEndCell = sheet.getRow(shiftEndRowIndex).getCell(shiftEndColumnIndex);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        while (continueCheckRow != null) {
            for (int i = 0; i < 12; i++) {
                if (!employeeNameStringValue.isBlank()) {

                    String[] parts = employeeNameStringValue.split(" ");
                    String firstName = parts[0];
                    String lastName = parts[1];

                    Employee employee = employeeRepository.findEmployeeByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
                    if (employee != null) {
                        for (int j = 0; j < 31; j++) {

                            boolean isDateOutOfBound = j + 1 <= LocalDate.now().plusMonths(1).lengthOfMonth();

                            if (isDateOutOfBound) {
                                if ((shiftStartCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(shiftStartCell))) {

                                    boolean isCorrectCellColor = isCorrectCellColor(shiftStartCell);

                                    if (isCorrectCellColor) {
                                        String shiftStartCellTime = formatter.format(shiftStartCell.getDateCellValue());
                                        LocalTime shiftStartLocalTime = LocalTime.parse(shiftStartCellTime);

                                        String shiftEndCellTime = formatter.format(shiftEndCell.getDateCellValue());
                                        LocalTime shiftEndLocalTime = LocalTime.parse(shiftEndCellTime);


                                        int day = j + 1;
                                        LocalDate shiftDate = LocalDate.of(year, month, day);

                                        String shiftName = calculateShiftName(shiftStartLocalTime);

                                        Shift shift = new Shift(shiftDate, shiftName, shiftStartLocalTime, shiftEndLocalTime);
                                        shift.setEmployee(employee);
                                        employee.addShift(shift);
                                        shiftRepository.save(shift);
                                        employeeRepository.save(employee);
                                    }
                                }
                            }
                            shiftStartRowIndex++;
                            shiftEndRowIndex++;
                            shiftStartCell = sheet.getRow(shiftStartRowIndex).getCell(shiftStartColumnIndex);
                            shiftEndCell = sheet.getRow(shiftEndRowIndex).getCell(shiftEndColumnIndex);
                        }
                    }
                }
                employeeNameColumnIndex = employeeNameColumnIndex + 3;
                employeeNameCell = sheet.getRow(employeeNameRowIndex).getCell(employeeNameColumnIndex);
                employeeNameStringValue = employeeNameCell.getStringCellValue();

                shiftStartRowIndex = employeeNameRowIndex + 5;
                shiftStartColumnIndex = employeeNameColumnIndex;
                shiftStartCell = sheet.getRow(shiftStartRowIndex).getCell(shiftStartColumnIndex);

                shiftEndRowIndex = shiftStartRowIndex;
                shiftEndColumnIndex = shiftStartColumnIndex + 1;
                shiftEndCell = sheet.getRow(shiftEndRowIndex).getCell(shiftEndColumnIndex);
            }

            continueRowIndex = continueRowIndex + 42;
            continueCheckRow = sheet.getRow(continueRowIndex);

            if (continueCheckRow != null) {
                employeeNameRowIndex = continueRowIndex;
                employeeNameColumnIndex = continueColumnIndex + 2;
                employeeNameCell = sheet.getRow(employeeNameRowIndex).getCell(employeeNameColumnIndex);
                employeeNameStringValue = employeeNameCell.getStringCellValue();


                shiftStartRowIndex = employeeNameRowIndex + 5;
                shiftStartColumnIndex = employeeNameColumnIndex;
                shiftStartCell = sheet.getRow(shiftStartRowIndex).getCell(shiftStartColumnIndex);


                shiftEndRowIndex = shiftStartRowIndex;
                shiftEndColumnIndex = shiftStartColumnIndex + 1;
                shiftEndCell = sheet.getRow(shiftEndRowIndex).getCell(shiftEndColumnIndex);
            }
        }
        workbook.close();
    }

    @Transactional
    @Override
    public void updateEmployeesWithCurrentShift() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Shift> currentEmployees = shiftRepository.findCurrentShifts(today, yesterday);
        Position breakPosition = positionRepository.findPositionByPositionName("przerwa");

        for (Shift shift : currentEmployees) {
            Employee employee = shift.getEmployee();
            if (employee.getPosition() == null) {
                if(!shift.isActive()){
                    shift.setActive(true);
                    shiftRepository.save(shift);
                }
                PositionEmployeeHistory history = new PositionEmployeeHistory(employee, breakPosition);
                history.setStartDate(shift.getWorkDate());
                history.setStartTime(shift.getStartTime());
                history.setActive(true);

                historyRepository.save(history);

                employee.setPosition(breakPosition);
            }
        }
    }

    @Override
    public List<List<Shift>> splitOnActiveAndInactive(List<Shift> shifts) {
        List<Shift> activeShifts = new ArrayList<>();
        List<Shift> inactiveShifts = new ArrayList<>();
        for(Shift shift:shifts){
            if(shift.isActive()){
                activeShifts.add(shift);
            }else{
                inactiveShifts.add(shift);
            }
        }
        List<List<Shift>> result = new ArrayList<>();
        result.add(activeShifts);
        result.add(inactiveShifts);

        return result;
    }


    private void deleteScheduleIfExists(int month, int year){
        if(shiftRepository.existsByMonthAndYear(month, year)){
            List<Shift> shiftsToDelete = shiftRepository.getShiftsByMonthAndYear(month, year);
            for (Shift shift:shiftsToDelete) {
                shift.getEmployee().getShifts().remove(shift);
            }

            shiftRepository.deleteAll(shiftsToDelete);
        }
    }

    private String calculateShiftName(LocalTime shiftStartTime){
        String shiftName;
        if(shiftStartTime.getHour() <= 9){
            shiftName = "Zmiana poranna";
        } else if(shiftStartTime.getHour() <= 13){
            shiftName = "Zmiana popołudniowa";
        } else{
            shiftName = "Zmiana nocna";
        }
        return shiftName;
    }

    private boolean isCorrectCellColor(Cell shiftStartCell) {
        CellStyle shiftStartCellStyle = shiftStartCell.getCellStyle();
        XSSFColor startColor = ((XSSFCellStyle) shiftStartCellStyle).getFillForegroundColorColor();

        boolean shouldAddCell = false;

        if(startColor == null){
            shouldAddCell = true;

        } else {
            String startHexColor = startColor.getARGBHex();

            if(!startHexColor.equals("FF00B0F0") && !startHexColor.equals("FF00B050")){
                shouldAddCell = true;
            }
        }
        return shouldAddCell;
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
