package me.serebryakov.weatherbot.repository;

import me.serebryakov.weatherbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
