package me.serebryakov.weatherbot.exceptions;

/**
 * Исключение, которое выбрасывается, когда запрашиваемый пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not exist!");
    }
}
