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
    @Column(name = "time_experience_in_minutes")
    private int timeExperienceInMinutes;

    public EmployeeSkill() {
    }

    public EmployeeSkill(Employee employee, Skill skill) {
        this.employee = employee;
        this.skill = skill;
        proficiencyLevel = 0;
        timeExperienceInMinutes = 0;
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

    public int getTimeExperienceInMinutes() {
        return timeExperienceInMinutes;
    }

    public void setTimeExperienceInMinutes(int timeExperienceInMinutes) {
        this.timeExperienceInMinutes = timeExperienceInMinutes;
    }

    public void addExperience(int minutes) {
        this.timeExperienceInMinutes = this.timeExperienceInMinutes + minutes;
    }
}
