package wang.zhongpin.pi.model.weather;

import java.util.List;

public class DailyWeather {
    private List<Weather> weathers;

    public DailyWeather(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public void addWeather(Weather weather) {
        this.weathers.add(weather);
    }


    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }
}
