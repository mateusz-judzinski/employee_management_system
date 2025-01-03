package employee.management.system.entity;

import employee.management.system.entity.Employee;
import employee.management.system.entity.Position;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "position_employee_history")
public class PositionEmployeeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "is_active")
    private boolean isActive;

    public PositionEmployeeHistory() {
    }

    public PositionEmployeeHistory(Employee employee, Position position) {
        this.employee = employee;
        this.position = position;
        startDate = LocalDate.now();
        startTime = LocalTime.now();
        isActive = true;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "PositionEmployeeHistory{" +
                "id=" + id +
                ", employee=" + employee +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
