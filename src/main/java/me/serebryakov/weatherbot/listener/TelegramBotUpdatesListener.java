package me.serebryakov.weatherbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import me.serebryakov.weatherbot.entity.User;
import me.serebryakov.weatherbot.service.impl.UserServiceImpl;
import me.serebryakov.weatherbot.botService.WeatherBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс TelegramBotUpdatesListener отвечает за обработку обновлений (updates) от Telegram-бота и реагирование на команды пользователей.
 * Он слушает входящие обновления, обрабатывает сообщения и предоставляет информацию о погоде в зависимости от взаимодействия с пользователем.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    // Константное приветственное сообщение для пользователей
    private final String HELLO_MESSAGE = "Привет! Я - бот, который поможет вам узнать текущую погоду " +
            "и прогноз на день по выбранному городу. Просто отправьте название города," +
            " и я пришлю вам актуальную информацию о погоде. Удачного использования!";

    // Пункты меню, отображаемые пользователю
    private final String[] MENU = {"Изменить город", "Погода сейчас", "Прогноз на день"};
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ExecutorService executorService;
    private final WeatherBotService weatherBotService;
    private final UserServiceImpl userService;

    /**
     * Конструктор класса TelegramBotUpdatesListener с переданными зависимостями.
     *
     * @param telegramBot       Экземпляр TelegramBot для отправки ответов пользователю.
     * @param weatherBotService Экземпляр WeatherBotService для обработки операций, связанных с погодой.
     * @param userService       Экземпляр UserServiceImpl для обработки данных пользователей.
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot, WeatherBotService weatherBotService, UserServiceImpl userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(10);
        this.weatherBotService = weatherBotService;
    }

    /**
     * Инициализация слушателя обновлений Telegram-бота путем установки его на экземпляр TelegramBot.
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Обработка входящих обновлений от Telegram-бота.
     *
     * @param list Список обновлений, полученных от Telegram-бота.
     * @return Целочисленное значение, указывающее статус обработки обновлений.
     */
    @Override
    public int process(List<Update> list) {
        executorService.submit(() -> {
            try {
                list.forEach(update -> {
                    logger.info("handles update: {}", update);
                    Message message = update.message();
                    String text = message.text();
                    long chatId = message.chat().id();
                    SendPhoto sendPhoto;
                    SendMessage sendMessage;
                    SendResponse sendResponse;

                    // Убедимся, что пользователь существует в базе данных
                    if (userService.findByChatId(chatId) == null) {
                        userService.update(new User(chatId, "", false));
                    }

                    // Обработка команд и взаимодействия с пользователем
                    if (("/start").equalsIgnoreCase(text) || ("старт").equalsIgnoreCase(text)) {
                        sendResponse = telegramBot.execute(helloMessage(chatId));
                    } else if (("Выбрать город").equalsIgnoreCase(text) || ("Изменить город").equalsIgnoreCase(text)) {
                        sendMessage = new SendMessage(chatId, "Введите название города:");
                        userService.update(new User(chatId, "", true));
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if (("Погода сейчас").equalsIgnoreCase(text)) {
                        if (!userService.findByChatId(chatId).getCity().equals("")) {
                            sendPhoto = weatherBotService.getCurrent(chatId);
                            sendResponse = telegramBot.execute(sendPhoto);
                        } else {
                            sendResponse = telegramBot.execute(selectCityMessage(chatId));
                        }
                    } else if ("Прогноз на день".equalsIgnoreCase(text)) {
                        if (!userService.findByChatId(chatId).getCity().equals("")) {
                            sendResponse = telegramBot.execute(getForecast(chatId));
                        } else {
                            sendResponse = telegramBot.execute(selectCityMessage(chatId));
                        }
                    } else {
                        if (userService.findByChatId(chatId).isCityStatus()) {
                            if (weatherBotService.isCityExists(text)) {
                                sendMessage = citySelectedMessage(chatId, text);
                            } else {
                                sendMessage = new SendMessage(chatId, "К сожалению, такого города нет. Выберите другой:");
                            }
                        } else {
                            sendMessage = wrongCommandMessage(chatId);
                        }
                        sendResponse = telegramBot.execute(sendMessage);
                    }

                    if (!sendResponse.isOk()) {
                        logger.error("Error sending message: {}", sendResponse.description());
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Создает и возвращает сообщение с просьбой выбрать город из предоставленного меню.
     *
     * @param chatId Уникальный идентификатор чата с пользователем.
     * @return Объект SendMessage с текстом "Сначала выберите город!" и меню для выбора.
     */
    private SendMessage selectCityMessage(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
        return new SendMessage(chatId, "Сначала выберите город!").replyMarkup(replyKeyboardMarkup);
    }

    /**
     * Создает и возвращает сообщение о выборе города с обновленными данными о пользователе.
     *
     * @param chatId Уникальный идентификатор чата с пользователем.
     * @param city   Название выбранного города.
     * @return Объект SendMessage с текстом "Выбран город: " + city и меню для выбора.
     */
    private SendMessage citySelectedMessage(long chatId, String city) {
        userService.update(new User(chatId, city, false));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
        return new SendMessage(chatId, "Выбран город: " + city).replyMarkup(replyKeyboardMarkup);
    }

    /**
     * Получает прогноз погоды на один день и возвращает соответствующее сообщение с прогнозом и меню для выбора.
     *
     * @param chatId Уникальный идентификатор чата с пользователем.
     * @return Объект SendMessage с прогнозом погоды на один день и меню для выбора.
     */
    private SendMessage getForecast(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
        return weatherBotService.getForecast(chatId, 1).replyMarkup(replyKeyboardMarkup);
    }

    /**
     * Создает и возвращает сообщение о неверной команде с предоставленным меню для выбора.
     *
     * @param chatId Уникальный идентификатор чата с пользователем.
     * @return Объект SendMessage с текстом "Неверная команда! Попробуйте ещё раз!" и меню для выбора.
     */
    private SendMessage wrongCommandMessage(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
        return new SendMessage(chatId, "Неверная команда! Попробуйте ещё раз!").replyMarkup(replyKeyboardMarkup);
    }

    /**
     * Создает и возвращает объект SendMessage с приветственным сообщением и клавиатурой для выбора действий.
     *
     * @param chatId Идентификатор чата пользователя, которому будет отправлено приветственное сообщение.
     * @return Объект SendMessage с приветственным сообщением и клавиатурой для выбора действий.
     */
    private SendMessage helloMessage(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup("Выбрать город");
        return new SendMessage(chatId, HELLO_MESSAGE).replyMarkup(replyKeyboardMarkup);
    }
}
