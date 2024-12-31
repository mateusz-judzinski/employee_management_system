package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import employee.management.system.repository.EmployeeRepository;
import employee.management.system.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService{

    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, EmployeeRepository employeeRepository) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Position findPositionById(int id) {
        return positionRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Position with id: " + id + " not found"));
    }

    @Override
    public List<Position> findAllPositions() {
        List<Position> positions = positionRepository.findAll();
        for (Position position : positions) {
            sortEmployeesByStartTime(position);
        }

        return positions;
    }

    @Transactional
    @Override
    public void addPosition(Position position) {
        positionRepository.save(position);
    }

    @Transactional
    @Override
    public void updatePosition(Position position) {
        positionRepository.save(position);
    }

    @Transactional
    @Override
    public void deletePositionById(int id) {
        if(!positionRepository.existsById(id)){
            throw new RuntimeException("Position with id: " + id + " not found");
        }
        positionRepository.deleteById(id);
    }

    @Override
    public Position findPositionByName(String name) {
        Position position = positionRepository.findPositionByPositionName(name);
        sortEmployeesByStartTime(position);

        return position;
    }

    @Transactional
    @Override
    public void switchActiveStatusByPositionId(int positionId) {
        Position position = positionRepository.findById(positionId).orElseThrow(() ->
                new RuntimeException("Position with id: " + positionId + " not found"));
        position.setIsActive(!position.getIsActive());

        for (Employee employee : position.getEmployees()) {
            employee.setPosition(positionRepository.findPositionByPositionName("przerwa"));
            employee.setPositionStartTime(LocalDateTime.now());
        }
        position.setEmployees(null);
    }

    @Transactional
    @Override
    public void addEmployeesIntoPosition(int positionId, List<Integer> employeesIds) {
        Position position = positionRepository.findById(positionId).orElseThrow(() ->
                new RuntimeException("Position with id: " + positionId + " not found"));

        List<Employee> updatedEmployees = position.getEmployees();
        List<Employee> employees = employeeRepository.findAllById(employeesIds);

        updatedEmployees.addAll(employees);
        position.setEmployees(updatedEmployees);

        for (Employee employee : employees) {
            employee.setPosition(position);
            employee.setPositionStartTime(LocalDateTime.now());
        }
    }
    @Transactional
    @Override
    public void removeEmployeeFromPositionByEmployeeId(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new RuntimeException("Employee with id: " + employeeId + " not found"));;
        Position position = employee.getPosition();

        employee.setPosition(positionRepository.findPositionByPositionName("przerwa"));
        employee.setPositionStartTime(LocalDateTime.now());

        position.getEmployees().remove(employee);
    }

    private void sortEmployeesByStartTime(Position position) {
        if (position.getEmployees() != null) {
            position.getEmployees().sort(Comparator.comparing(Employee::getPositionStartTime));
        }
    }
}
