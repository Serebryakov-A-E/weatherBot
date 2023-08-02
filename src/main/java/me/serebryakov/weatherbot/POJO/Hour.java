package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий прогноз погоды на конкретный час.
 */
@Data
public class Hour {

    /**
     * Дата и время прогноза в формате timestamp (эпоха).
     */
    private long time_epoch;

    /**
     * Дата и время прогноза в формате "YYYY-MM-DD HH:mm".
     */
    private String time;

    /**
     * Температура в градусах Цельсия.
     */
    private double temp_c;

    /**
     * Температура в градусах Фаренгейта.
     */
    private double temp_f;

    /**
     * Флаг, указывающий, что сейчас день (1) или ночь (0).
     */
    private int is_day;

    /**
     * Объект, содержащий информацию о текущих условиях погоды.
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
     * Направление ветра.
     */
    private String wind_dir;

    /**
     * Давление в гектопаскалях.
     */
    private double pressure_mb;

    /**
     * Давление в дюймах ртутного столба.
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
     * Влажность в процентах.
     */
    private int humidity;

    /**
     * Облачность в процентах.
     */
    private int cloud;

    /**
     * Ощущаемая температура в градусах Цельсия.
     */
    private double feelslike_c;

    /**
     * Ощущаемая температура в градусах Фаренгейта.
     */
    private double feelslike_f;

    /**
     * Индекс охлаждения от ветра в градусах Цельсия.
     */
    private double windchill_c;

    /**
     * Индекс охлаждения от ветра в градусах Фаренгейта.
     */
    private double windchill_f;

    /**
     * Коэффициент теплового индекса в градусах Цельсия.
     */
    private double heatindex_c;

    /**
     * Коэффициент теплового индекса в градусах Фаренгейта.
     */
    private double heatindex_f;

    /**
     * Точка росы в градусах Цельсия.
     */
    private double dewpoint_c;

    /**
     * Точка росы в градусах Фаренгейта.
     */
    private double dewpoint_f;

    /**
     * Флаг, указывающий, будет ли идти дождь (1) или нет (0).
     */
    private int will_it_rain;

    /**
     * Вероятность дождя в процентах.
     */
    private int chance_of_rain;

    /**
     * Флаг, указывающий, будет ли идти снег (1) или нет (0).
     */
    private int will_it_snow;

    /**
     * Вероятность снега в процентах.
     */
    private int chance_of_snow;

    /**
     * Видимость в километрах.
     */
    private double vis_km;

    /**
     * Видимость в милях.
     */
    private double vis_miles;

    /**
     * Скорость порывов ветра в милях в час.
     */
    private double gust_mph;

    /**
     * Скорость порывов ветра в километрах в час.
     */
    private double gust_kph;

    /**
     * Ультрафиолетовый индекс.
     */
    private double uv;
}
