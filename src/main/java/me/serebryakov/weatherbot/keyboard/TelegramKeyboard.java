package me.serebryakov.weatherbot.keyboard;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
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

    public SendPhoto getCurrent(Message message) {
        long chatId = message.chat().id();
        String city = message.text();
        WeatherData weatherData = weatherApiClient.getCurrentWeatherData(city);

        if (weatherData == null) {
            return new SendPhoto(chatId, getImageBytesByUrl("https://cdn-icons-png.flaticon.com/512/5219/5219070.png")).caption("Такого города нет.");
        }

        double currentTemp = weatherData.getCurrent().getTemp_c();
        String condition = weatherData.getCurrent().getCondition().getText();

        String imageUrl = weatherData.getCurrent().getCondition().getIcon();
        SendPhoto sendPhoto = new SendPhoto(chatId, getImageBytesByUrl(imageUrl));
        String messageText = "Погода в городе " + city + ": " +
                "\nТекущая температура: " + currentTemp + "°C " +
                "\nСостояние: " + condition;

        sendPhoto.caption(messageText);

        return sendPhoto;
    }

    public SendMessage getForecast(Message message, int days) {
        long chatId = message.chat().id();
        String city = message.text();
        WeatherData weatherData = weatherApiClient.getForeCastsWeatherData(city, days);

        if (weatherData == null) {
            return new SendMessage(chatId, "Такого города нет.");
        }

        Double avgTemp = weatherData.getForecast().getForecastday().get(0).getDay().getAvgtemp_c();
        Double maxTemp = weatherData.getForecast().getForecastday().get(0).getDay().getMaxtemp_c();
        Double minTemp = weatherData.getForecast().getForecastday().get(0).getDay().getMintemp_c();

        StringBuilder sb = new StringBuilder();

        sb.append("Минимальная температура: ").append(minTemp).append("\n");
        sb.append("Средняя температура: ").append(avgTemp).append("\n");
        sb.append("Максимальная температура: ").append(maxTemp).append("\n");
        return new SendMessage(message.chat().id(), sb.toString());
    }
    private byte[] getImageBytesByUrl(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        if (imageUrl.startsWith("//")) {
            imageUrl = "https:" + imageUrl;
        }
        return restTemplate.getForObject(imageUrl, byte[].class);
    }
}
