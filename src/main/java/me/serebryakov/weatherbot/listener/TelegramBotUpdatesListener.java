package me.serebryakov.weatherbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
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

                    SendPhoto sendPhoto = telegramKeyboard.execute(message);

                    SendResponse sendResponse = telegramBot.execute(sendPhoto);

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
