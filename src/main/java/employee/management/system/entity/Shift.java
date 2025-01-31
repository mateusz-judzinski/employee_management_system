package employee.management.system.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "shift")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "work_date")
    private LocalDate workDate;
    @Column(name = "shift_name")
    private String shiftName;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "is_active")
    private boolean isActive;

    public Shift() {
    }

    public Shift(Shift shift){
        this.workDate = shift.getWorkDate();
        this.shiftName = shift.getShiftName();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.employee = shift.getEmployee();
        this.isActive = shift.isActive();
    }

    public Shift(LocalDate workDate, LocalTime startTime, LocalTime endTime) {
        this.workDate = workDate;
        this.shiftName = getShiftNameByShiftStartTime(startTime);
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getShiftNameByShiftStartTime(LocalTime shiftStartTime){
        String shiftName;
        if(shiftStartTime.getHour() <= 9){
            shiftName = "Zmiana poranna";
        } else if(shiftStartTime.getHour() <= 13){
            shiftName = "Zmiana popołudniowa";
        } else{
            shiftName = "Zmiana nocna";
        }
        return shiftName;
    }
}
