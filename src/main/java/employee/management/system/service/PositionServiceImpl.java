package employee.management.system.service;

import employee.management.system.entity.*;
import employee.management.system.repository.EmployeeRepository;
import employee.management.system.repository.EmployeeSkillRepository;
import employee.management.system.repository.PositionEmployeeHistoryRepository;
import employee.management.system.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionEmployeeHistoryRepository historyRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, EmployeeRepository employeeRepository, PositionEmployeeHistoryRepository historyRepository, EmployeeSkillRepository employeeSkillRepository) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.historyRepository = historyRepository;
        this.employeeSkillRepository = employeeSkillRepository;
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
        if (!positionRepository.existsById(id)) {
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
        Position breakPosition = positionRepository.findPositionByPositionName("przerwa");

        for (Employee employee : position.getEmployees()) {
            processHistory(employee, breakPosition);
            employee.setPosition(breakPosition);
        }

        if(position.isTemporary()){
            positionRepository.delete(position);
        }
        else{
            position.setEmployees(null);
        }
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
            processHistory(employee, position);
            employee.setPosition(position);
        }
    }

    @Transactional
    @Override
    public void removeEmployeeFromPositionByEmployeeId(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new RuntimeException("Employee with id: " + employeeId + " not found"));

        Position position = employee.getPosition();
        Position breakPosition = positionRepository.findPositionByPositionName("przerwa");

        processHistory(employee, breakPosition);
        employee.setPosition(breakPosition);

        position.getEmployees().remove(employee);
    }

    @Transactional
    @Override
    public void addTemporaryPosition(String positionName, String description) {

        Position temporaryPosition = new Position(positionName, description, true, true);
        positionRepository.save(temporaryPosition);
    }

    private void sortEmployeesByStartTime(Position position) {
        if (position.getEmployees() != null) {
            position.getEmployees().sort(Comparator.comparing(employee ->
                    historyRepository.findByEmployeeIdAndIsActiveTrue(employee.getId()).getStartTime()));
        }
    }

    @Transactional
    @Override
    public void processHistory(Employee employee, Position newPosition) {

        PositionEmployeeHistory history = historyRepository.findByEmployeeIdAndIsActiveTrue(employee.getId());

        LocalTime startTime = history.getStartTime();
        updateEmployeeSkillTimeExperience(employee, startTime);

        history.setEndTime(LocalTime.now());
        history.setActive(false);

        if(newPosition != null){
            if(newPosition.isTemporary()){
                Position otherPosition = positionRepository.findPositionByPositionName("inne");
                PositionEmployeeHistory newHistory = new PositionEmployeeHistory(employee, otherPosition);
                historyRepository.save(newHistory);
            }
            else{
                PositionEmployeeHistory newHistory = new PositionEmployeeHistory(employee, newPosition);
                historyRepository.save(newHistory);
            }
        }
    }

    @Override
    public List<Position> getPositionsForManagement() {
        return positionRepository.getPositionForManagement();
    }

    @Override
    public boolean existsByPositionName(String positionName) {
        return positionRepository.existsByPositionName(positionName);
    }

    @Override
    public List<Position> getPositionForAddForm() {
        return positionRepository.getPositionForAddForm();
    }

    @Override
    public List<Position> getPositionsByIds(List<Integer> ids) {
        return positionRepository.findAllById(ids);
    }

    @Override
    public List<Position> getPositionForEditForm(int skillId) {
        return positionRepository.getPositionForEditForm(skillId);
    }

    @Override
    public List<Position> findPositionBySkillId(int skillId) {
        return positionRepository.findPositionBySkillId(skillId);
    }

    @Override
    public List<Position> findPositionByQualificationId(int qualificationId) {
        return positionRepository.findPositionByQualificationId(qualificationId);
    }

    @Override
    public boolean isPositionNameOccupied(Position position) {

        Position nameOwner = positionRepository.findPositionByPositionName(position.getPositionName());

        if(nameOwner == null){
            return false;
        }
        else{
            return nameOwner.getId() != position.getId();
        }
    }

    private void updateEmployeeSkillTimeExperience(Employee employee, LocalTime startTime) {

        if (employee.getPosition().getSkill() != null) {
            int minutes = (int) Duration.between(startTime, LocalTime.now()).toMinutes();

            int employeeId = employee.getId();
            int skillId = employee.getPosition().getSkill().getId();

            EmployeeSkill employeeSkill = employeeSkillRepository.findEmployeeSkillByEmployeeIdAndSkillId(employeeId, skillId);
            employeeSkill.addExperience(minutes);

            employeeSkillRepository.save(employeeSkill);
        }
    }
}
