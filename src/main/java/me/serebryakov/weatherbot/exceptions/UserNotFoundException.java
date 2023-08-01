package me.serebryakov.weatherbot.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not exist!");
    }
}
