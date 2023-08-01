package me.serebryakov.weatherbot.service;

import me.serebryakov.weatherbot.entity.User;

public interface UserService {
    void create(User user);
    void update(User user);

    User findByChatId(long chatId);
}
