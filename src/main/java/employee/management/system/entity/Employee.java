package employee.management.system.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "id_card_number")
    private Integer idCardNumber;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
    @Column(name = "position_start_time")
    private LocalDateTime positionStartTime;
    @Column(name = "has_driving_licence")
    private boolean hasDrivingLicence;
    @Column(name = "can_work_in_luggage_room")
    private boolean canWorkInLuggageRoom;
    @OneToMany(mappedBy = "employee",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Shift> shifts;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, String phoneNumber, boolean hasDrivingLicence, boolean canWorkInLuggageRoom) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hasDrivingLicence = hasDrivingLicence;
        this.canWorkInLuggageRoom = canWorkInLuggageRoom;
        idCardNumber = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean hasDrivingLicence() {
        return hasDrivingLicence;
    }

    public void setHasDrivingLicence(boolean hasDrivingLicence) {
        this.hasDrivingLicence = hasDrivingLicence;
    }

    public boolean canWorkInLuggageRoom() {
        return canWorkInLuggageRoom;
    }

    public void setCanWorkInLuggageRoom(boolean canWorkInLuggageRoom) {
        this.canWorkInLuggageRoom = canWorkInLuggageRoom;
    }

    public Integer getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(Integer idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public LocalDateTime getPositionStartTime() {
        return positionStartTime;
    }

    public void setPositionStartTime(LocalDateTime positionStartTime) {
        this.positionStartTime = positionStartTime;
    }

    public Duration getDuration() {
        if(positionStartTime != null){
            return Duration.between(positionStartTime, LocalDateTime.now());
        }
        return Duration.ZERO;
    }

    public String getDurationFormatted() {
        if (positionStartTime != null) {
            Duration duration = Duration.between(positionStartTime, LocalDateTime.now());
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return "00:00:00";
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", idCardNumber=" + idCardNumber +
                '}';
    }

    public void addShift(Shift shift){

        if(shifts == null){
            shifts = new ArrayList<>();
        }
        shifts.add(shift);
        shift.setEmployee(this);
    }

    public void addSkill(Skill skill){

        if(skills == null){
            skills = new ArrayList<>();
        }
        skills.add(skill);
        skill.getEmployees().add(this);
    }
}
