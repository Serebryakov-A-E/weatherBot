package me.serebryakov.weatherbot.service;

import me.serebryakov.weatherbot.entity.User;

public interface UserService {

    /**
     * Обновляет информацию о существующем пользователе.
     *
     * @param user объект с данными пользователя, которого нужно обновить
     */
    void update(User user);

    /**
     * Ищет пользователя по его идентификатору чата.
     *
     * @param chatId идентификатор чата пользователя
     * @return найденный объект пользователя или null, если пользователь не найден
     */
    User findByChatId(long chatId);
}
