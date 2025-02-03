package employee.management.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Nazwa umiejętności nie może być pusta.")
    @Size(min = 3, max = 30, message = "Nazwa umiejętności musi mieć od 3 do 30 znaków.")
    @Column(name = "skill_name")
    private String skillName;
    @NotBlank(message = "Opis umiejętności nie może być pusty.")
    @Size(min = 10, max = 250, message = "Opis umiejętności może mieć od 10 do 250 znaków.")
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "skill",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Position> positions;
    @OneToMany(mappedBy = "skill",
            cascade = CascadeType.ALL)
    private List<EmployeeSkill> employees;

    public Skill() {
    }

    public Skill(String skillName, String description) {
        this.skillName = skillName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<EmployeeSkill> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeSkill> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", skillName='" + skillName + '\'' +
                ", description='" + description + '\'' +
                ", employees=" + employees +
                '}';
    }

    void addEmployee(EmployeeSkill employee){

        if(employees == null){
            employees = new ArrayList<>();
        }
        employees.add(employee);
        employee.setSkill(this);

    }

    void addPosition(Position position){
        if(positions == null){
            positions = new ArrayList<>();
        }
        positions.add(position);
        position.setSkill(this);
    }
}
