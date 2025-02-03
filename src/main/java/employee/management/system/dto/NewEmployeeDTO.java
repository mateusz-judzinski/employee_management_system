package employee.management.system.dto;

import employee.management.system.entity.Employee;
import employee.management.system.entity.EmployeeSkill;
import employee.management.system.entity.Qualification;
import jakarta.validation.Valid;

import java.util.List;

public class NewEmployeeDTO {
    @Valid
    private Employee employee;
    private List<EmployeeSkill> skills;

    private List<Qualification> qualifications;

    public NewEmployeeDTO(Employee employee, List<EmployeeSkill> skills, List<Qualification> qualifications) {
        this.employee = employee;
        this.skills = skills;
        this.qualifications = qualifications;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Qualification> qualifications) {
        this.qualifications = qualifications;
    }
}
