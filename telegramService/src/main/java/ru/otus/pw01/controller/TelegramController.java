package ru.otus.pw01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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
import ru.otus.pw01.model.ButtonType;
import ru.otus.pw01.model.TelegramUser;
import ru.otus.pw01.service.TelegramUserService;

import javax.annotation.PostConstruct;
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

        if (receivedMessageText != null && receivedMessageText.equals(configTelegram.getRegistrationButtonText())) {
            registerUser(chatId);
            return;
        }

        if (receivedMessageText != null && receivedMessageText.equals(configTelegram.getGenerateOTPButtonText())) {
            //send message to webOTPService
            logger.debug("to do send message to webOTPService");
            SendMessage message = new SendMessage(chatId, "Wait for receive one-time password (response time ~1 min)");
            sendToUser(message, chatId, "responseText");
            return;
        }

        Contact contact = receivedMessage.getContact();
        if (contact != null) {
            processContact(contact,receivedMessage.getFrom());
            generateOTP(chatId);
            return;
        }

        TelegramUser user = telegramUserService.findUserByUserID(Long.valueOf(receivedMessage.getFrom().getId()));
        if (user != null) {
            generateOTP(chatId);
        } else {
            registerUser(chatId);
        }
        logger.debug("random input");

    }

    @Override
    public String getBotToken() {
        return configTelegram.getBotToken();
    }


    @Override
    public String getBotUsername() {
        return configTelegram.getBotName();
    }

    private void registerUser(long chatId) {
        SendMessage message = new SendMessage(chatId,configTelegram.getRegistrationChatMessage() );
        message.setReplyMarkup(prepareRegistrationButton(configTelegram.getRegistrationButtonText(),ButtonType.REQUEST_CONTACT));
        sendToUser(message, chatId, "registerUser done");
    }

    private void generateOTP(long chatId) {
        SendMessage message = new SendMessage(chatId,configTelegram.getGenerateOTPChatMessage() );
        message.setReplyMarkup(prepareRegistrationButton(configTelegram.getGenerateOTPButtonText(),ButtonType.REQUEST_TEXT));
        sendToUser(message, chatId, "generateOTP done");
    }


    private ReplyKeyboardMarkup prepareRegistrationButton(String buttonText, ButtonType buttonType) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(createKeyboardRow(buttonText,buttonType));
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private KeyboardRow createKeyboardRow(String buttonText, ButtonType buttonType) {
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton;
        switch (buttonType){
            case REQUEST_TEXT:
                keyboardButton = new KeyboardButton(buttonText).setRequestContact(false);
                break;
            case REQUEST_CONTACT:
                keyboardButton = new KeyboardButton(buttonText).setRequestContact(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + buttonType);
        }
        row.add(keyboardButton);
        return row;
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
    private void saveContact(Contact contact, User sender ) {
       TelegramUser newUser = new TelegramUser();
        newUser.setUserID(Long.valueOf(contact.getUserID()));
        newUser.setFirstName(contact.getFirstName());
        newUser.setLastName(contact.getLastName());
        newUser.setPhoneNumber(contact.getPhoneNumber());
        newUser.setvCard(contact.getVCard());
        newUser.setUserName(sender.getUserName());
        newUser.setBot(sender.getBot());
        newUser.setLanguageCode(sender.getLanguageCode());
        telegramUserService.saveUserIfNotExist(newUser);
    }

    /**
     * Processes received contact
     *
     * @param contact - received contact
     * @param sender  - sender
     */
    private void processContact(Contact contact, User sender) {
       TelegramUser user = telegramUserService.findUserByPhoneNumber(contact.getPhoneNumber());
        if (user == null) {
            saveContact(contact,sender);
        }
    }
}
