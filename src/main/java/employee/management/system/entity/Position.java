package employee.management.system.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(mappedBy = "position",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.DETACH, CascadeType.REFRESH})
    private List<Employee> employees;

    public Position() {
    }

    public Position(String positionName, String description, boolean isActive) {
        this.positionName = positionName;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", positionName='" + positionName + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    public void addEmployee(Employee employee){
        if(employees == null){
            employees = new ArrayList<>();
        }
        employees.add(employee);
    }
}
