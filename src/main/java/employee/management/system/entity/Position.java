package employee.management.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Nazwa stanowiska nie może być pusta.")
    @Size(min = 3, max = 30, message = "Nazwa stanowiska musi mieć od 3 do 30 znaków.")
    @Column(name = "position_name")
    private String positionName;
    @NotBlank(message = "Opis stanowiska nie może być pusty.")
    @Size(min = 10, max = 250, message = "Opis stanowiska może mieć od 10 do 250 znaków.")
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_temporary")
    private boolean isTemporary;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "skill_id")
    private Skill skill;
    @OneToMany(mappedBy = "position",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.DETACH, CascadeType.REFRESH})
    private List<Employee> employees;
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "position_qualification",
            joinColumns = @JoinColumn(name = "position_id"),
            inverseJoinColumns = @JoinColumn(name = "qualification_id")
    )
    private List<Qualification> neededQualifications;
    public Position() {
    }

    public Position(String positionName, String description, boolean isActive, boolean isTemporary) {
        this.positionName = positionName;
        this.description = description;
        this.isActive = isActive;
        this.isTemporary = isTemporary;
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

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Qualification> getNeededQualifications() {
        return neededQualifications;
    }

    public void setNeededQualifications(List<Qualification> neededQualifications) {
        this.neededQualifications = neededQualifications;
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
        employee.setPosition(this);
    }

    public void addQualification(Qualification qualification){
        if(neededQualifications == null){
            neededQualifications = new ArrayList<>();
        }
        neededQualifications.add(qualification);
        qualification.getPositions().add(this);
    }
}
