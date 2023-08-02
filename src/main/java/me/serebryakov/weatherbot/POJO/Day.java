package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий прогноз погоды на день.
 */
@Data
public class Day {

    /**
     * Максимальная температура в градусах Цельсия.
     */
    private double maxtemp_c;

    /**
     * Максимальная температура в градусах Фаренгейта.
     */
    private double maxtemp_f;

    /**
     * Минимальная температура в градусах Цельсия.
     */
    private double mintemp_c;

    /**
     * Минимальная температура в градусах Фаренгейта.
     */
    private double mintemp_f;

    /**
     * Средняя температура в градусах Цельсия.
     */
    private double avgtemp_c;

    /**
     * Средняя температура в градусах Фаренгейта.
     */
    private double avgtemp_f;

    /**
     * Максимальная скорость ветра в милях в час.
     */
    private double maxwind_mph;

    /**
     * Максимальная скорость ветра в километрах в час.
     */
    private double maxwind_kph;

    /**
     * Общее количество осадков в миллиметрах.
     */
    private double totalprecip_mm;

    /**
     * Общее количество осадков в дюймах.
     */
    private double totalprecip_in;

    /**
     * Общее количество снега в сантиметрах.
     */
    private double totalsnow_cm;

    /**
     * Средняя видимость в километрах.
     */
    private double avgvis_km;

    /**
     * Средняя видимость в милях.
     */
    private double avgvis_miles;

    /**
     * Средняя влажность воздуха в процентах.
     */
    private double avghumidity;

    /**
     * Флаг, указывающий, будет ли дождь в этот день (1 - да, 0 - нет).
     */
    private int daily_will_it_rain;

    /**
     * Вероятность дождя в процентах.
     */
    private int daily_chance_of_rain;

    /**
     * Флаг, указывающий, будет ли снег в этот день (1 - да, 0 - нет).
     */
    private int daily_will_it_snow;

    /**
     * Вероятность снега в процентах.
     */
    private int daily_chance_of_snow;

    /**
     * Объект, представляющий условия погоды.
     */
    private Condition condition;

    /**
     * Индекс УФ-излучения.
     */
    private double uv;
}
