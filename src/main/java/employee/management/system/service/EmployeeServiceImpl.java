package employee.management.system.service;

import employee.management.system.entity.Employee;
import employee.management.system.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
}
