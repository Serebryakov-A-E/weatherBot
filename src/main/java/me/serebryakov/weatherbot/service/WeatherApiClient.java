package me.serebryakov.weatherbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.serebryakov.weatherbot.POJO.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiClient {
    @Value("${weather.api.key}")
    private String weatherApiKey;

    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";
    private static final String CITY = "Orsk";
    private static final String AQI = "no";

    public WeatherData getWeatherData() {
        String apiUrl = BASE_URL + "?key=" + weatherApiKey + "&q=" + CITY + "&aqi=" + AQI;
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // Создание ObjectMapper для преобразования JSON в объекты Java
        ObjectMapper objectMapper = new ObjectMapper();

        // Преобразование JSON в объект WeatherData
        try {
            // Теперь вы можете использовать объект weatherData для получения данных о погоде
            return objectMapper.readValue(jsonResponse, WeatherData.class);
        } catch (Exception e) {
            // Обработка ошибок, если что-то пошло не так при преобразовании
            e.printStackTrace();
        }
        return null;
    }
}
