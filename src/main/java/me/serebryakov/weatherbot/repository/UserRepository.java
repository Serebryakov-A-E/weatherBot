package me.serebryakov.weatherbot.repository;

import me.serebryakov.weatherbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для доступа к данным о пользователях.
 * Этот интерфейс наследует от {@link org.springframework.data.jpa.repository.JpaRepository},
 * что позволяет выполнять базовые операции с сущностью {@link User} в базе данных.
 * Репозиторий предоставляет методы для сохранения, обновления и поиска пользователей.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
