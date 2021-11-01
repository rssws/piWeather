package wang.zhongpin.pi.model.weather;

import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;

public class HourlyWeatherResponse extends Response {
    private HourlyWeather hourlyWeather;
    private Coord coord;

    public HourlyWeatherResponse(ResponseStatus responseStatus, HourlyWeather hourlyWeather, Coord coord) {
        this(responseStatus, "", hourlyWeather, coord);
    }

    public HourlyWeatherResponse(ResponseStatus responseStatus, String responseMessage, HourlyWeather hourlyWeather, Coord coord) {
        super(responseStatus, responseMessage);
        this.hourlyWeather = hourlyWeather;
        this.coord = coord;
    }

    public HourlyWeather getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(HourlyWeather hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
