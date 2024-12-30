package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.entity.Shift;
import employee.management.system.repository.EmployeeRepository;
import employee.management.system.repository.PositionRepository;
import employee.management.system.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ShiftRepository shiftRepository, PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.positionRepository = positionRepository;
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
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
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
    public List<Employee> getShiftEmployees(LocalDate localDate) {

        List<Shift> shifts = shiftRepository.findShiftsByWorkDate(localDate);
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

    @Transactional
    @Override
    public void removeEmployeeFromPositionByEmployeeId(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new RuntimeException("Employee with id: " + employeeId + " not found"));;
        Position position = employee.getPosition();

        employee.setPosition(positionRepository.findPositionByPositionName("przerwa"));
        position.getEmployees().remove(employee);
    }
}
