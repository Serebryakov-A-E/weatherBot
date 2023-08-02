package me.serebryakov.weatherbot.telegrammBotConfig;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для создания и настройки Telegram-бота.
 * Этот класс предоставляет методы для создания бина TelegramBot, который используется
 * для взаимодействия с Telegram API.
 * Информация о токене бота считывается из файла конфигурации и передается в созданный
 * экземпляр TelegramBot.
 */
@Configuration
public class TelegramBotConfiguration {
    @Value("${telegramm.bot.token}")
    private String token;

    /**
     * Создает и настраивает экземпляр TelegramBot для взаимодействия с Telegram API.
     * @return экземпляр TelegramBot с настроенным токеном бота
     */
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(token);
    }
}
