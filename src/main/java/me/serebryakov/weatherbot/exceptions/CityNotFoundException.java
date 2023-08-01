package me.serebryakov.weatherbot.exceptions;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String message) {
        super(message);
    }
}
