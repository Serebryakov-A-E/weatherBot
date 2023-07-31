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
import me.serebryakov.weatherbot.keyboard.TelegramKeyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final String HELLO_MESSAGE = "Добро пожаловать! Я бот, который поможет вам узнать текущую" +
            " погоду в различных городах. Просто отправьте название города, и я пришлю вам актуальную " +
            "информацию о погоде. Попробуйте отправить название города, например, \"Москва\" или \"Лондон\". " +
            "Удачного использования!";
    private final String[] DAY_FORECAST = new String[]{"Сегодня", "Завтра", "Послезавтра"
    };
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ExecutorService executorService;
    private final TelegramKeyboard telegramKeyboard;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramKeyboard telegramKeyboard) {
        this.telegramBot = telegramBot;
        this.executorService = Executors.newFixedThreadPool(10);
        this.telegramKeyboard = telegramKeyboard;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        executorService.submit(() -> {
            try {
                list.forEach(update -> {
                    logger.info("handles update: {}", update);
                    Message message = update.message();
                    SendPhoto sendPhoto;
                    SendMessage sendMessage;
                    SendResponse sendResponse;

                    if (("/start").equalsIgnoreCase(message.text()) || ("старт").equalsIgnoreCase(message.text())) {
                        sendMessage = new SendMessage(message.chat().id(), HELLO_MESSAGE);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if ("Прогноз".equalsIgnoreCase(message.text())) {
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(DAY_FORECAST);
                        sendMessage = new SendMessage(message.chat().id(), "Выберите на какой день вы хотите получить прогноз:")
                                .replyMarkup(replyKeyboardMarkup);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if ("Орск".equalsIgnoreCase(message.text())) {
                        // TODO: сделать возможность выбора прогноза по дате, для этого нужно сохранять город и давать на выбор дату
                        sendMessage = telegramKeyboard.getForecast(message, 1);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if ("Затвтра".equalsIgnoreCase(message.text())) {
                        sendMessage = telegramKeyboard.getForecast(message, 2);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if ("Послезавтра".equalsIgnoreCase(message.text())) {
                        sendMessage = telegramKeyboard.getForecast(message, 3);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else {
                        sendPhoto = telegramKeyboard.getCurrent(message);
                        sendResponse = telegramBot.execute(sendPhoto);
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
}
