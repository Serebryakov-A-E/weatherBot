package me.serebryakov.weatherbot.exceptions;

/**
 * Исключение, которое выбрасывается, когда запрашиваемый город не найден.
 */
public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException() {
        super("City not found");
    }
}
