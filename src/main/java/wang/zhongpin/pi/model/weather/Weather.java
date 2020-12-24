package wang.zhongpin.pi.model.weather;

public class Weather {
    private int dt;
    private String description;
    private String icon;
    private Double temp;
    private Double tempMax;
    private Double tempMin;

    public Weather(int dt, String description, String icon, Double temp, Double tempMax, Double tempMin) {
        this.dt = dt;
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

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }
}
