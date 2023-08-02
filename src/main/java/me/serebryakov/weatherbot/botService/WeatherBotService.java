package me.serebryakov.weatherbot.botService;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import me.serebryakov.weatherbot.POJO.Hour;
import me.serebryakov.weatherbot.POJO.WeatherData;
import me.serebryakov.weatherbot.service.WeatherApiClient;
import me.serebryakov.weatherbot.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Класс TelegramKeyboard предоставляет методы для работы с клавиатурой и отправки сообщений пользователю через Telegram бота.
 * Класс взаимодействует с внешним API погодного сервиса (WeatherApiClient) и сервисом пользователей (UserServiceImpl).
 */
@Service
public class WeatherBotService {
    private final WeatherApiClient weatherApiClient;
    private final UserServiceImpl userService;

    public WeatherBotService(WeatherApiClient weatherApiClient, UserServiceImpl userService) {
        this.weatherApiClient = weatherApiClient;
        this.userService = userService;
    }

    /**
     * Получает текущую погоду для заданного чата.
     *
     * @param chatId идентификатор чата, для которого запрашивается погода.
     * @return объект SendPhoto, содержащий изображение с информацией о текущей погоде и текстовым описанием.
     *         Если город не найден или данные о погоде недоступны, возвращается изображение с информацией о ошибке.
     */
    public SendPhoto getCurrent(long chatId) {
        String city = userService.findByChatId(chatId).getCity();
        WeatherData weatherData = weatherApiClient.getCurrentWeatherData(city);

        if (weatherData == null) {
            // Если город не найден, возвращаем изображение с информацией о ошибке
            return new SendPhoto(chatId, getImageBytesByUrl("https://cdn-icons-png.flaticon.com/512/5219/5219070.png")).caption("Такого города нет.");
        }

        // Получаем данные о текущей погоде
        double currentTemp = weatherData.getCurrent().getTemp_c();
        String condition = weatherData.getCurrent().getCondition().getText();

        // Получаем ссылку на иконку погоды и формируем сообщение с информацией о погоде
        String imageUrl = weatherData.getCurrent().getCondition().getIcon();
        SendPhoto sendPhoto = new SendPhoto(chatId, getImageBytesByUrl(imageUrl));
        String messageText = "Погода в городе " + city + ": " +
                "\nТекущая температура: " + currentTemp + "°C " +
                "\nСостояние: " + condition;

        sendPhoto.caption(messageText);

        return sendPhoto;
    }

    /**
     * Получает прогноз погоды для указанного города и отправляет его пользователю с указанным chatId.
     *
     * @param chatId Уникальный идентификатор чата с пользователем.
     * @param days   Количество дней для прогноза (1 - на сегодня, 2 - на завтра, и т.д.).
     * @return Объект SendMessage с текстовым сообщением, содержащим прогноз погоды для указанного города.
     */
    public SendMessage getForecast(long chatId, int days) {
        String city = userService.findByChatId(chatId).getCity();
        WeatherData weatherData = weatherApiClient.getForeCastsWeatherData(city, days);

        if (weatherData == null || weatherData.getForecast() == null || weatherData.getForecast().getForecastday().isEmpty()) {
            return new SendMessage(chatId, "Извините, не удалось получить прогноз погоды для данного города.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Прогноз погоды для города ").append(city).append(":\n");
        sb.append("Сегодня:\n");
        sb.append("Минимальная температура: ").append(weatherData.getForecast().getForecastday().get(0).getDay().getMintemp_c()).append("°C\n");
        sb.append("Средняя температура: ").append(weatherData.getForecast().getForecastday().get(0).getDay().getAvgtemp_c()).append("°C\n");
        sb.append("Максимальная температура: ").append(weatherData.getForecast().getForecastday().get(0).getDay().getMaxtemp_c()).append("°C\n\n");

        List<Hour> hourlyForecast = weatherData.getForecast().getForecastday().get(0).getHour();

        int now = LocalDateTime.now().getHour();

        for (int i = now; i < hourlyForecast.size(); i++) {
            Hour hour = hourlyForecast.get(i);
            String forecastTime = weatherData.getForecast().getForecastday().get(0).getHour().get(i).getTime();
            LocalDateTime dateTime = LocalDateTime.parse(forecastTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

            sb.append("Время: ").append(formattedTime).append("\n");
            sb.append("Температура: ").append(hour.getTemp_c()).append("°C\n");
            sb.append("Вероятность дождя: ").append(hour.getChance_of_rain()).append("%\n");
            sb.append("Погода: ").append(hour.getCondition().getText()).append("\n\n");
        }

        return new SendMessage(chatId, sb.toString());
    }

    /**
     * Метод isCityExists() проверяет существование города в погодном сервисе путем запроса информации о текущей погоде
     * для данного города. Он использует объект WeatherApiClient для получения данных о погоде по переданному названию города.
     * Если полученный объект WeatherData не равен null, то город считается существующим, и метод возвращает true.
     * В противном случае метод возвращает false.
     *
     * @param city Название города, который нужно проверить на существование в погодном сервисе
     * @return true, если город существует, и false, если город не существует или возникла ошибка при получении данных
     */
    public boolean isCityExists(String city) {
        WeatherData weatherData = weatherApiClient.getCurrentWeatherData(city);
        return weatherData != null;
    }

    /**
     * Метод getImageBytesByUrl() выполняет HTTP GET запрос к переданному URL изображения. Если переданный URL начинается с "http://",
     * то метод выполняет запрос к указанному URL. Если URL начинается с "//", метод добавляет протокол "https:" к URL и выполняет запрос.
     * Полученное изображение представляется в виде массива байтов и возвращается из метода.
     *
     * @param imageUrl URL изображения, которое необходимо получить в виде массива байтов
     * @return Массив байтов, представляющий изображение, или null, если произошла ошибка при получении изображения
     */
    private byte[] getImageBytesByUrl(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        if (imageUrl.startsWith("//")) {
            imageUrl = "https:" + imageUrl;
        }
        return restTemplate.getForObject(imageUrl, byte[].class);
    }
}
