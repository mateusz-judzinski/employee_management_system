package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.EmployeeSkill;
import employee.management.system.entity.PositionEmployeeHistory;
import employee.management.system.entity.Shift;
import employee.management.system.repository.EmployeeRepository;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import employee.management.system.repository.ShiftRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final PositionService positionService;
    private final PositionEmployeeHistoryRepository historyRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ShiftRepository shiftRepository, PositionService positionService, PositionEmployeeHistoryRepository historyRepository) {
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.positionService = positionService;
        this.historyRepository = historyRepository;
    }
    @Override
    public Employee findEmployeeById(int id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Employee with id: " + id + " not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void deleteEmployeeById(int id) {
        if(!employeeRepository.existsById(id)){
            throw new RuntimeException("Employee with id: " + id + " not found");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getShiftEmployees(LocalDate today) {

        LocalDate yesterday = today.minusDays(1);
        List<Shift> shifts = shiftRepository.findCurrentShifts(today, yesterday);
        List<Employee> employees = new ArrayList<>();

        for (Shift shift:shifts) {
            employees.add(shift.getEmployee());
        }

        return employees;
    }

    @Override
    public List<Employee> findEmployeesBySkillId(int skillId) {
        return employeeRepository.findEmployeesBySkillId(skillId);
    }


    @Scheduled(cron = "0 1/30 * * * *")
    @Transactional
    @Override
    public void removePositionFromEmployeeAfterShift() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Employee> employeeWithPositionToRemove = employeeRepository.findEmployeesThatFinishedShift(today, yesterday);
        if(employeeWithPositionToRemove != null){
            for (Employee employee:employeeWithPositionToRemove) {

                if(employee.getPosition() != null){
                    positionService.processHistory(employee, null);
                    employee.setPosition(null);
                }
            }
        }
        List<Shift> shiftsToDeactivate = shiftRepository.findFinishedShiftsWithActiveOnTrue(today, yesterday);
        if(shiftsToDeactivate != null){
            for(Shift shift: shiftsToDeactivate){
                shift.setActive(false);
            }
        }
    }
    @PostConstruct
    @Override
    public void initDebug() {
        List<Employee> employees = employeeRepository.findByPositionIsNotNull();
        for (Employee employee:employees) {
            employee.setPosition(null);
            employeeRepository.save(employee);
        }
        List<PositionEmployeeHistory> histories = historyRepository.findActivePositionEmployeeHistories();
        for (PositionEmployeeHistory history:histories) {
            historyRepository.delete(history);
        }
    }

    @Override
    public List<Employee> getAllEmployeesSortedByLastName() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public List<Employee> getEmployeesFromSearchBarByOneElement(String firstOrLastName, String lastOrFirstName) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstOrLastName, lastOrFirstName);
    }

    @Override
    public List<Employee> getEmployeesFromSearchBarByTwoElements(String firstName, String lastName) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
    }

    @Override
    public List<Employee> findEmployeesWithCurrentShift() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        return employeeRepository.findEmployeesWithCurrentShifts(today, yesterday);
    }

    @Override
    public boolean existsByIdCardNumber(String idCardNumber) {
        return employeeRepository.existsByIdCardNumber(idCardNumber);
    }

    @Override
    public List<Employee> findEmployeesByQualificationId(int qualificationId) {
        return employeeRepository.findEmployeesByQualificationId(qualificationId);
    }

    @Override
    public boolean isIdCardOccupied(Employee employee) {

        Employee idCardOwner = employeeRepository.findEmployeeByIdCardNumber(employee.getIdCardNumber());

        if(idCardOwner == null){
            return false;
        }
        else{
            return idCardOwner.getId() != employee.getId();
        }
    }
}
