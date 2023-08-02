package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий информацию о местоположении.
 */
@Data
public class Location {

    /**
     * Название местоположения (город, населенный пункт и т.д.).
     */
    private String name;

    /**
     * Регион, к которому относится местоположение.
     */
    private String region;

    /**
     * Страна, в которой находится местоположение.
     */
    private String country;

    /**
     * Широта местоположения.
     */
    private double lat;

    /**
     * Долгота местоположения.
     */
    private double lon;

    /**
     * Идентификатор временной зоны местоположения.
     */
    private String tz_id;

    /**
     * Время местоположения в формате timestamp (эпоха).
     */
    private long localtime_epoch;

    /**
     * Локальное время местоположения в формате "YYYY-MM-DD HH:mm".
     */
    private String localtime;
}