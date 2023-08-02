package me.serebryakov.weatherbot.service.impl;

import me.serebryakov.weatherbot.entity.User;
import me.serebryakov.weatherbot.exceptions.UserNotFoundException;
import me.serebryakov.weatherbot.repository.UserRepository;
import me.serebryakov.weatherbot.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для управления пользователями.
 * Этот класс реализует интерфейс {@link UserService} и предоставляет методы для обновления данных
 * пользователя и поиска пользователя по идентификатору чата.
 * Данные пользователей сохраняются с помощью объекта {@link UserRepository}.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Обновляет информацию о пользователе.
     * Если пользователь с таким идентификатором чата уже существует, то его данные будут обновлены.
     * Если пользователь не существует, то он будет создан с переданными данными.
     *
     * @param user объект с данными пользователя, которого нужно обновить или создать
     */
    @Override
    public void update(User user) {
        repository.save(user);
    }


    /**
     * Ищет пользователя по его идентификатору чата.
     *
     * @param chatId идентификатор чата пользователя
     * @return найденный объект пользователя или null, если пользователь не найден
     */
    @Override
    public User findByChatId(long chatId) {
        try {
            return repository.findById(chatId).orElseThrow(UserNotFoundException::new);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
