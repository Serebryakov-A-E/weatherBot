package me.serebryakov.weatherbot.POJO;

import lombok.Data;

import java.util.List;

@Data
public class ForecastDay {
    private String date;
    private long date_epoch;
    private Day day;
    private Astro astro;
    private List<Hour> hour;
}
