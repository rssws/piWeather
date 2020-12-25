package wang.zhongpin.pi.model.weather;

import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;

public class WeatherResponse extends Response {
    private Weather weather;
    private Coord coord;
    private String name;

    public WeatherResponse(ResponseStatus responseStatus, Weather weather, Coord coord, String name) {
        this(responseStatus, "", weather, coord, name);
    }

    public WeatherResponse(ResponseStatus responseStatus, String responseMessage, Weather weather, Coord coord, String name) {
        super(responseStatus, responseMessage);
        this.weather = weather;
        this.coord = coord;
        this.name = name;
    }

    public Weather getWeather() {
        return weather;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
