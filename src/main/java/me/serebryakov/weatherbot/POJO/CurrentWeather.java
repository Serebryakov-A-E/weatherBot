package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий текущие погодные данные.
 */
@Data
public class CurrentWeather {

    /**
     * Время последнего обновления данных в формате timestamp (эпоха).
     */
    private long last_updated_epoch;

    /**
     * Время последнего обновления данных в формате строки.
     */
    private String last_updated;

    /**
     * Температура в градусах Цельсия.
     */
    private double temp_c;

    /**
     * Температура в градусах Фаренгейта.
     */
    private double temp_f;

    /**
     * Флаг, указывающий, день или ночь в данный момент (1 - день, 0 - ночь).
     */
    private int is_day;

    /**
     * Объект, представляющий условия погоды.
     */
    private Condition condition;

    /**
     * Скорость ветра в милях в час.
     */
    private double wind_mph;

    /**
     * Скорость ветра в километрах в час.
     */
    private double wind_kph;

    /**
     * Направление ветра в градусах.
     */
    private int wind_degree;

    /**
     * Направление ветра в буквенном формате.
     */
    private String wind_dir;

    /**
     * Атмосферное давление в миллибарах.
     */
    private double pressure_mb;

    /**
     * Атмосферное давление в дюймах ртутного столба.
     */
    private double pressure_in;

    /**
     * Количество осадков в миллиметрах.
     */
    private double precip_mm;

    /**
     * Количество осадков в дюймах.
     */
    private double precip_in;

    /**
     * Влажность воздуха в процентах.
     */
    private int humidity;

    /**
     * Облачность в процентах.
     */
    private int cloud;

    /**
     * Температура ощущается как в градусах Цельсия.
     */
    private double feelslike_c;

    /**
     * Температура ощущается как в градусах Фаренгейта.
     */
    private double feelslike_f;

    /**
     * Видимость в километрах.
     */
    private double vis_km;

    /**
     * Видимость в милях.
     */
    private double vis_miles;

    /**
     * Индекс УФ-излучения.
     */
    private double uv;

    /**
     * Скорость порывов ветра в милях в час.
     */
    private double gust_mph;

    /**
     * Скорость порывов ветра в километрах в час.
     */
    private double gust_kph;
}
