package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий информацию о астрономических событиях.
 */
@Data
public class Astro {

    /**
     * Время восхода солнца.
     */
    private String sunrise;

    /**
     * Время заката солнца.
     */
    private String sunset;

    /**
     * Время восхода луны.
     */
    private String moonrise;

    /**
     * Время заката луны.
     */
    private String moonset;

    /**
     * Фаза луны.
     */
    private String moon_phase;

    /**
     * Освещенность луны (в процентах).
     */
    private String moon_illumination;

    /**
     * Флаг, указывающий, что луна видима (1) или нет (0).
     */
    private int is_moon_up;

    /**
     * Флаг, указывающий, что солнце видимо (1) или нет (0).
     */
    private int is_sun_up;
}
