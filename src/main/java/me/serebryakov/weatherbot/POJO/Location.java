package me.serebryakov.weatherbot.POJO;

import lombok.Data;

@Data
public class Location {
    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    private String tz_id;
    private long localtime_epoch;
    private String localtime;
}