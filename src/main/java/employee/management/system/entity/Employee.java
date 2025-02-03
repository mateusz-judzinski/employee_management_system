package employee.management.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Imię nie może być puste")
    @Size(min = 2, max = 20, message = "Imię musi mieć od 2 do 20 znaków")
    @Column(name = "first_name")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 35, message = "Nazwisko musi mieć od 2 do 35 znaków")
    @Column(name = "last_name")
    private String lastName;
    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Nieprawidłowy format adresu email")
    @Size(max = 100, message = "Email nie może przekraczać 100 znaków")
    @Column(name = "email")
    private String email;
    @Pattern(regexp = "\\d{9}", message = "Numer telefonu musi zawierać dokładnie 9 cyfr")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "id_card_number")
    private Integer idCardNumber;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private List<Shift> shifts;
    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private List<EmployeeSkill> skills;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "employee_qualification",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "qualification_id")
    )
    private List<Qualification> qualifications;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public boolean hasAllNeededQualification(Position position) {
        if (position == null || position.getNeededQualifications() == null || position.getNeededQualifications().isEmpty()) {
            return true;
        }
        return qualifications.containsAll(position.getNeededQualifications());
    }

    public int getSkillProficiencyLevelForPosition(Position position){
        Skill positionSkill = position.getSkill();
        int proficiencyLevel = -1;

        for (EmployeeSkill employeeSkill:skills) {
            if(employeeSkill.getSkill() == positionSkill){
                proficiencyLevel = employeeSkill.getProficiencyLevel();
            }
        }
        return proficiencyLevel;
    }

    public List<Shift> getIncomingShifts(){
        List<Shift> incomingShifts = new ArrayList<>();

        for (Shift shift:shifts){
            if(shift.getWorkDate().isAfter(LocalDate.now()) || shift.getWorkDate().isEqual(LocalDate.now())){
                incomingShifts.add(shift);
            } else if (shift.getWorkDate().isEqual(LocalDate.now().minusDays(1)) && shift.getStartTime().isAfter(shift.getEndTime())) {
                incomingShifts.add(shift);
            }
        }

        incomingShifts.sort(Comparator.comparing(Shift::getWorkDate));

        return incomingShifts;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
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

    public void addSkill(EmployeeSkill skill){

        if(skills == null){
            skills = new ArrayList<>();
        }
        skills.add(skill);
        skill.setEmployee(this);
    }

    public void addQualification(Qualification qualification){

        if(qualifications == null){
            qualifications = new ArrayList<>();
        }
        qualifications.add(qualification);
        qualification.getEmployees().add(this);
    }
    public boolean hasQualificationWithName(String qualificationName) {
        if (qualifications == null) {
            return false;
        }
        for (Qualification qualification:qualifications) {
            if(Objects.equals(qualification.getName(), qualificationName)){
                return true;
            }
        }
        return false;
    }

}
