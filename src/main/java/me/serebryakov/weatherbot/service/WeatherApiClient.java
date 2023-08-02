package me.serebryakov.weatherbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.serebryakov.weatherbot.POJO.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Класс WeatherApiClient предоставляет методы для получения данных о погоде с помощью API погодного сервиса.
 * Для взаимодействия с API сервиса используется библиотека RestTemplate.
 * Класс содержит два метода для получения текущей погоды и прогноза на несколько дней для заданного города.
 * Для выполнения запросов к API используется токен, который считывается из файла конфигурации.
 */
@Service
public class WeatherApiClient {
    @Value("${weather.api.key}")
    private String weatherApiKey;

    private static final String CURRENT_URL = "http://api.weatherapi.com/v1/current.json";
    private static final String FORECAST_URL = "http://api.weatherapi.com/v1/forecast.json";
    private static final String AQI = "no";

    /**
     * Получает данные о текущей погоде для заданного города.
     *
     * @param city название города
     * @return WeatherData объект, содержащий информацию о текущей погоде
     */
    public WeatherData getCurrentWeatherData(String city) {
        String apiUrl = CURRENT_URL + "?key=" + weatherApiKey + "&q=" + city + "&aqi=" + AQI;
        return sendResponseToApi(apiUrl);
    }

    /**
     * Получает данные о прогнозе погоды на несколько дней для заданного города.
     *
     * @param city название города
     * @param days количество дней для прогноза
     * @return WeatherData объект, содержащий информацию о прогнозе погоды
     */
    public WeatherData getForeCastsWeatherData(String city, int days) {
        String apiUrl = FORECAST_URL + "?key=" + weatherApiKey + "&q=" + city + "&days="+ days + "&aqi=" + AQI;
        return sendResponseToApi(apiUrl);
    }

    /**
     * Метод sendResponseToApi отправляет HTTP GET запрос к API погодного сервиса, используя переданный URL.
     * Затем он получает ответ в формате JSON и преобразует его в объект типа WeatherData с помощью библиотеки
     * ObjectMapper. В случае возникновения исключения в процессе выполнения запроса или преобразования JSON,
     * метод печатает стек вызовов и возвращает значение null.
     *
     * @param apiUrl URL для отправки запроса к API погодного сервиса
     * @return WeatherData объект, содержащий информацию о погоде, или null в случае ошибки
     */
    private WeatherData sendResponseToApi(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = "";
        try {
            jsonResponse = restTemplate.getForObject(apiUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, WeatherData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
