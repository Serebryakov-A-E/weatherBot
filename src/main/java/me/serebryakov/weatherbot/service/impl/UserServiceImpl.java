package me.serebryakov.weatherbot.service.impl;

import me.serebryakov.weatherbot.entity.User;
import me.serebryakov.weatherbot.exceptions.UserNotFoundException;
import me.serebryakov.weatherbot.repository.UserRepository;
import me.serebryakov.weatherbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(User user) {
        repository.save(user);
    }

    @Override
    public void update(User user) {
        repository.save(user);
    }

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
