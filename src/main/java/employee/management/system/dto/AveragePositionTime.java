package employee.management.system.dto;

public class AveragePositionTime {
    private String positionName;
    private String averageTime;
    private double percentage;

    public AveragePositionTime(String positionName, String averageTime, double percentage) {
        this.positionName = positionName;
        this.averageTime = averageTime;
        this.percentage = percentage;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(String averageTime) {
        this.averageTime = averageTime;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
