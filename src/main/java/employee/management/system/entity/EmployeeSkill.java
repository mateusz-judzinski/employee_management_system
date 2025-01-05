package employee.management.system.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "employee_skill")
public class EmployeeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
    @Column(name = "proficiency_level")
    private int proficiencyLevel;
    @Column(name = "time_experience")
    private LocalTime timeExperience;

    public EmployeeSkill() {
    }

    public EmployeeSkill(Employee employee, Skill skill) {
        this.employee = employee;
        this.skill = skill;
        proficiencyLevel = 0;
        timeExperience = LocalTime.parse("00:00:00");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(int proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public LocalTime getTimeExperience() {
        return timeExperience;
    }

    public void setTimeExperience(LocalTime timeExperience) {
        this.timeExperience = timeExperience;
    }

    public void addExperience(int hours, int minutes, int seconds) {
        this.timeExperience = this.timeExperience.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }
}
