package me.serebryakov.weatherbot.POJO;

import lombok.Data;

import java.util.List;

/**
 * Класс, представляющий прогноз погоды.
 */
@Data
public class Forecast {

    /**
     * Список объектов прогноза на разные дни.
     */
    private List<ForecastDay> forecastday;
}
