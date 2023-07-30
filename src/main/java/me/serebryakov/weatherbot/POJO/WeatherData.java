package me.serebryakov.weatherbot.POJO;

import lombok.Data;

@Data
public class WeatherData {
    private Location location;
    private CurrentWeather current;
}
