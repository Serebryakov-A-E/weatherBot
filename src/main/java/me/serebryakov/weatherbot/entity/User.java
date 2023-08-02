package me.serebryakov.weatherbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий пользователя бота.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    /**
     * Идентификатор чата с пользователем.
     */
    @Id
    private long chatId;

    /**
     * Название города, выбранного пользователем для получения погоды.
     */
    @Column(name = "city")
    private String city;

    /**
     * Статус выбора города. True, если пользователь находится в режиме выбора города, иначе false.
     */
    @Column(name = "city_status")
    private boolean cityStatus;
}
