package me.serebryakov.weatherbot.keyboard;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import me.serebryakov.weatherbot.POJO.WeatherData;
import me.serebryakov.weatherbot.service.WeatherApiClient;
import org.springframework.stereotype.Service;

@Service
public class TelegramKeyboard {
    private final WeatherApiClient weatherApiClient;

    public TelegramKeyboard(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    public SendMessage execute(Message message) {
        long chatId = message.chat().id();
        WeatherData weatherData = weatherApiClient.getWeatherData();

        String city = weatherData.getLocation().getName();
        double currentTemp = weatherData.getCurrent().getTemp_c();
        String condition = weatherData.getCurrent().getCondition().getText();

        String messageText = "Погода в городе " + city + ": " +
                "Текущая температура: " + currentTemp + "°C" +
                "Состояние: " + condition;

        return new SendMessage(chatId, messageText);
    }
}
