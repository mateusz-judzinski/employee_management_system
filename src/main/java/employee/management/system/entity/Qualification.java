package employee.management.system.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "qualification")
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "qualification_name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "qualifications",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Employee> employees;
    @ManyToMany(mappedBy = "neededQualifications",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Position> positions;

    public Qualification() {
    }

    public Qualification(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }


    public void addPosition(Position position){

        if(positions == null){
            positions = new ArrayList<>();
        }
        positions.add(position);
        position.getNeededQualifications().add(this);
    }

    public void addEmployee(Employee employee){

        if(employees == null){
            employees = new ArrayList<>();
        }
        employees.add(employee);
        employee.getQualifications().add(this);
    }
}
