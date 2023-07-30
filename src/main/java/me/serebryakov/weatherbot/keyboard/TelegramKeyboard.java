package me.serebryakov.weatherbot.keyboard;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendPhoto;
import me.serebryakov.weatherbot.POJO.WeatherData;
import me.serebryakov.weatherbot.service.WeatherApiClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramKeyboard {
    private final WeatherApiClient weatherApiClient;

    public TelegramKeyboard(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    public SendPhoto execute(Message message) {
        long chatId = message.chat().id();
        String city = message.text();
        WeatherData weatherData = weatherApiClient.getWeatherData(city);

        double currentTemp = weatherData.getCurrent().getTemp_c();
        String condition = weatherData.getCurrent().getCondition().getText();

        String imageUrl = weatherData.getCurrent().getCondition().getIcon();
        SendPhoto sendPhoto = new SendPhoto(chatId, getImageBytesByUrl(imageUrl));
        String messageText = "Погода в городе " + city + ": " +
                "Текущая температура: " + currentTemp + "°C" +
                "Состояние: " + condition;

        sendPhoto.caption(messageText);

        return sendPhoto;
    }

    private byte[] getImageBytesByUrl(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        if (imageUrl.startsWith("//")) {
            imageUrl = "https:" + imageUrl;
        }
        return restTemplate.getForObject(imageUrl, byte[].class);
    }
}
