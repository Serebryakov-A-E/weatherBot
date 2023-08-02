package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий информацию о погоде.
 */
@Data
public class WeatherData {

    /**
     * Местоположение, для которого предоставляется информация о погоде.
     */
    private Location location;

    /**
     * Текущая погода в указанном местоположении.
     */
    private CurrentWeather current;

    /**
     * Прогноз погоды на несколько дней для указанного местоположения.
     */
    private Forecast forecast;
}
