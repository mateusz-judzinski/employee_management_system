package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Skill;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    Employee findEmployeeById(int id);
    List<Employee> getAllEmployees();
    void addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployeeById(int id);
    List<Employee> getShiftEmployees(LocalDate localDate);
    List<Employee> findEmployeesBySkillId(int skillId);
    void removePositionFromEmployeeAfterShift();
    void initDebug();
    List<Employee> getAllEmployeesSortedByLastName();
    List<Employee> getEmployeesFromSearchBarByOneElement(String firstOrLastName, String lastOrFirstName);
    List<Employee> getEmployeesFromSearchBarByTwoElements(String firstName, String lastName);
    List<Employee> findEmployeesWithCurrentShift();
}
