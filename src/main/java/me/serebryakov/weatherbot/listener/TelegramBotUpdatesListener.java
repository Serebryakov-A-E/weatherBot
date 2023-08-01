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
import me.serebryakov.weatherbot.keyboard.TelegramKeyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final String HELLO_MESSAGE = "Привет! Я - бот, который поможет вам узнать текущую погоду " +
            "и прогноз на день по выбранному городу. Просто отправьте название города," +
            " и я пришлю вам актуальную информацию о погоде. Удачного использования!";

    private final String[] MENU = {"Изменить город", "Погода сейчас", "Прогноз на день"};
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ExecutorService executorService;
    private final TelegramKeyboard telegramKeyboard;
    private final UserServiceImpl userService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramKeyboard telegramKeyboard, UserServiceImpl userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
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
                    String text = message.text();
                    long chatId = message.chat().id();
                    SendPhoto sendPhoto;
                    SendMessage sendMessage;
                    SendResponse sendResponse;

                    if (userService.findByChatId(chatId) == null) {
                        userService.create(new User(chatId, "", false));
                    }

                    if (("/start").equalsIgnoreCase(text) || ("старт").equalsIgnoreCase(text)) {
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup("Выбрать город");
                        sendMessage = new SendMessage(chatId, HELLO_MESSAGE).replyMarkup(replyKeyboardMarkup);
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if (("Выбрать город").equalsIgnoreCase(text) || ("Изменить город").equalsIgnoreCase(text)) {
                        sendMessage = new SendMessage(chatId, "Введите название города:");
                        userService.update(new User(chatId, "", true));
                        sendResponse = telegramBot.execute(sendMessage);
                    } else if (("Погода сейчас").equalsIgnoreCase(text)) {
                        if (!userService.findByChatId(chatId).getCity().equals("")) {
                            sendPhoto = telegramKeyboard.getCurrent(message);
                            sendResponse = telegramBot.execute(sendPhoto);
                        } else {
                            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
                            sendMessage = new SendMessage(chatId, "Сначала выберите город!").replyMarkup(replyKeyboardMarkup);
                            sendResponse = telegramBot.execute(sendMessage);
                        }
                    } else if ("Прогноз на день".equalsIgnoreCase(text)) {
                        if (!userService.findByChatId(chatId).getCity().equals("")) {
                            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
                            sendMessage = telegramKeyboard.getForecast(message, 1).replyMarkup(replyKeyboardMarkup);
                            sendResponse = telegramBot.execute(sendMessage);
                        } else {
                            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
                            sendMessage = new SendMessage(chatId, "Сначала выберите город!").replyMarkup(replyKeyboardMarkup);
                            sendResponse = telegramBot.execute(sendMessage);
                        }
                    } else {
                        if (userService.findByChatId(chatId).isCityStatus()) {
                            if (telegramKeyboard.isCityExists(text)) {
                                userService.update(new User(chatId, text, false));
                                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
                                sendMessage = new SendMessage(chatId, "Выбран город: " + text).replyMarkup(replyKeyboardMarkup);
                            } else {
                                sendMessage = new SendMessage(chatId, "К сожалению, такого города нет. Выберите другой:");
                            }
                        } else {
                            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(MENU);
                            sendMessage = new SendMessage(chatId, "Неверная команда! Попробуйте ещё раз!").replyMarkup(replyKeyboardMarkup);
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
}
