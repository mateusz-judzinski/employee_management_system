package employee.management.system.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

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

    public String getSkillProficiencyName(){
        if(proficiencyLevel == 3){
            return "zaawansowany";
        } else if(proficiencyLevel == 2){
            return "średnio zaawansowany";
        } else if (proficiencyLevel == 1) {
            return "początkujący";
        } else if(proficiencyLevel == 0){
            return "brak doświadoczenia";
        } else{
            return "błąd";
        }
    }

    public void setTimeExperienceByProficiencyLevel(int proficiencyLevel){
        switch (proficiencyLevel){
            case 3 -> timeExperienceInMinutes = 9000;
            case 2 -> timeExperienceInMinutes = 3600;
            case 1 -> timeExperienceInMinutes = 1200;
            default -> timeExperienceInMinutes = 0;
        }
    }

}
