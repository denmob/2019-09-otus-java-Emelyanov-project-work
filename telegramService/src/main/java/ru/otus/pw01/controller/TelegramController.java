package ru.otus.pw01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.pw01.config.TelegramConfig;
import ru.otus.pw01.model.TelegramUser;
import ru.otus.pw01.service.TelegramUserService;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component("telegramBot")
public class TelegramController extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);

    private final TelegramConfig configTelegram;
    private final TelegramUserService telegramUserService;

    public TelegramController(TelegramUserService telegramUserService, TelegramConfig configTelegram) {
        this.telegramUserService = telegramUserService;
        this.configTelegram = configTelegram;
    }


    @PostConstruct
    public void registerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error("registerBot TelegramApiException", e);
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        String receivedMessageText = receivedMessage.getText();
        long chatId = receivedMessage.getChatId();
        Contact contact = receivedMessage.getContact();
        if (contact != null) {
            processContact(contact, chatId, receivedMessage.getFrom());
        } else {
           // if (receivedMessageText == null || !receivedMessageText.equals("/start"))
                // to do
           // else initializeUser(chatId);
        }
    }

    @Override
    public String getBotToken() {
        return configTelegram.getBotToken();
    }


    @Override
    public String getBotUsername() {
        return configTelegram.getBotName();
    }

    /**
     * Initialize user for future work(sending notification)
     *
     * @param chatId - id of the chat between user and bot
     */
    private void initializeUser(long chatId) {

        SendMessage message = new SendMessage(chatId, "");

        TelegramUser user = telegramUserService.findUserByTelegramUserId(chatId);

        message.setReplyMarkup(prepareKeyboardMarkup(configTelegram.getRegistrationButton()));

        sendToUser(message, chatId, "");
    }

    /**
     * Creates and setups ReplyKeyboardMarkup witch one button
     *
     * @param buttonText - button text
     * @return - prepared ReplyKeyboardMarkup
     */
    private ReplyKeyboardMarkup prepareKeyboardMarkup(String buttonText) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(buttonText).setRequestContact(true));
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Deletes keyboard wich all buttons
     *
     * @param chatId      - target chat id
     * @param messageText - message text
     */
    private void hideKeyboardButtons(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        message.setReplyMarkup(replyKeyboardRemove);
        sendToUser(message, chatId, messageText);
    }

    /**
     * Sends message to user
     *
     * @param message     - message
     * @param chatId      - chat id
     * @param messageText - a text of the message
     */
    private void sendToUser(SendMessage message, long chatId, String messageText)  {
        try {
            execute(message);
            logger.debug("Sent message {} to {}", messageText, chatId);
        } catch (Exception e) {
            logger.error("Failed to sendNotificationToMail message {} to {} due to error {}", messageText, chatId, e.getMessage());
        }
    }


    /**
     * Saves contact if it not exists
     *
     * @param contact - contact, that need to be saved
     * @param sender  - user's data, that sent this contact
     */
    private void saveContact(Contact contact, User sender) {
       TelegramUser newUser = new TelegramUser();
        newUser.setTelegramUserId(Long.valueOf(contact.getUserID()));
        newUser.setFirstName(contact.getFirstName());
        newUser.setLastName(contact.getLastName());
        newUser.setUserName(sender.getUserName());
        newUser.setBot(sender.getBot());
        newUser.setLanguageCode(sender.getLanguageCode());
        newUser.setPhoneNumber(contact.getPhoneNumber());
        telegramUserService.saveUserIfNotExist(newUser);
    }

    /**
     * Processes received contact
     *
     * @param contact - received contact
     * @param chatId  - chat id
     * @param sender  - sender
     */
    private void processContact(Contact contact, long chatId, User sender) {

    }


}
