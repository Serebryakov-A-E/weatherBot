package me.serebryakov.weatherbot.POJO;

import lombok.Data;

import java.util.List;

/**
 * Класс, представляющий прогноз погоды на конкретный день.
 */
@Data
public class ForecastDay {

    /**
     * Дата прогноза в формате "YYYY-MM-DD".
     */
    private String date;

    /**
     * Дата прогноза в формате timestamp (эпоха).
     */
    private long date_epoch;

    /**
     * Объект, содержащий информацию о погоде в течение дня.
     */
    private Day day;

    /**
     * Объект, содержащий астрономические данные для данного дня (время восхода/захода солнца и луны и т.д.).
     */
    private Astro astro;

    /**
     * Список объектов прогноза погоды на протяжении часа.
     */
    private List<Hour> hour;
}
