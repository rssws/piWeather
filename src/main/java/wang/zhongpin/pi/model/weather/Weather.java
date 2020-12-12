package wang.zhongpin.pi.model.weather;

public class Weather {
    private String description;
    private String icon;
    private double temp;
    private double tempMax;
    private double tempMin;

    public Weather(String description, String icon, double temp, double tempMax, double tempMin) {
        this.description = description;
        this.icon = icon;
        this.temp = temp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }
}
