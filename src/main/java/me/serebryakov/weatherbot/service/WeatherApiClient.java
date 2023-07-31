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

    private static final String CURRENT_JSON = "http://api.weatherapi.com/v1/current.json";
    private static final String FORECAST_URL = "http://api.weatherapi.com/v1/forecast.json";
    private static final String AQI = "no";

    public WeatherData getCurrentWeatherData(String city) {
        String apiUrl = CURRENT_JSON + "?key=" + weatherApiKey + "&q=" + city + "&aqi=" + AQI;
        return sendResponseToApi(apiUrl);
    }

    public WeatherData getForeCastsWeatherData(String city, int days) {
        String apiUrl = FORECAST_URL + "?key=" + weatherApiKey + "&q=" + city + "&days="+ days + "&aqi=" + AQI;
        return sendResponseToApi(apiUrl);
    }

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
