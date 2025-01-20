package employee.management.system.dto;

public class AveragePositionTime {
    private String positionName;
    private String timeSpent;
    private double percentage;

    public AveragePositionTime(String positionName, String timeSpent, double percentage) {
        this.positionName = positionName;
        this.timeSpent = timeSpent;
        this.percentage = percentage;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
