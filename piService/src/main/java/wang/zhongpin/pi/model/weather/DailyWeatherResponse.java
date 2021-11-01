package wang.zhongpin.pi.model.weather;

import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;

public class DailyWeatherResponse extends Response {
    private DailyWeather dailyWeather;
    private Coord coord;

    public DailyWeatherResponse(ResponseStatus responseStatus, DailyWeather dailyWeather, Coord coord) {
        this(responseStatus, "", dailyWeather, coord);
    }

    public DailyWeatherResponse(ResponseStatus responseStatus, String responseMessage, DailyWeather dailyWeather, Coord coord) {
        super(responseStatus, responseMessage);
        this.dailyWeather = dailyWeather;
        this.coord = coord;
    }

    public DailyWeather getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(DailyWeather dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
