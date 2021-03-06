package ru.otus.pw01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
import ru.otus.pw.library.message.CommandType;
import ru.otus.pw01.config.TelegramConfig;
import ru.otus.pw01.model.AllowedUser;
import ru.otus.pw01.service.AllowedUserService;
import ru.otus.pw01.model.TelegramUser;
import ru.otus.pw01.service.TelegramUserService;
import ru.otus.pw01.sokets.SocketClient;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TelegramController extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);

    final TelegramConfig configTelegram;
    private final TelegramUserService telegramUserService;
    private final AllowedUserService allowedUserService;
    private final SocketClient socketClient;

    public TelegramController(TelegramUserService telegramUserService, TelegramConfig configTelegram, AllowedUserService allowedUserService, SocketClient socketClient) {
        this.telegramUserService = telegramUserService;
        this.configTelegram = configTelegram;
        this.allowedUserService = allowedUserService;
        this.socketClient = socketClient;
    }

    @PostConstruct
    public void registerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        String receivedMessageText = receivedMessage.getText();
        long chatId = receivedMessage.getChatId();

        if (receivedMessageText != null) logger.debug("onUpdateReceived with: {}", receivedMessageText);

        if (receivedMessageText != null && receivedMessageText.equals(configTelegram.getRegistrationButtonText())) {
            registerUser(chatId);
            return;
        }

        if (receivedMessageText != null && receivedMessageText.equals(configTelegram.getGenerateOTPButtonText())) {
            SendMessage message = new SendMessage(chatId, configTelegram.getMessageWaitOTP());
            sendToUser(message);
            socketClient.sendMessage(CommandType.GENERATE_OTP, String.valueOf(chatId), null);
            return;
        }

        Contact contact = receivedMessage.getContact();
        if (contact != null) {
            AllowedUser allowedUser = allowedUserService.findAllowedUserByPhoneNumber(contact.getPhoneNumber());
            if (allowedUser != null) {
                processContact(contact, receivedMessage.getFrom());
                socketClient.sendMessage(CommandType.SAVE_USER_DATA, String.valueOf(chatId), contact);
                generateOTP(chatId);
            } else {
                hideKeyboardButtons(chatId, configTelegram.getMessageNotAllowedPhoneNumber());
            }
            return;
        }

        TelegramUser telegramUser = telegramUserService.findTelegramUserByUserID(Long.valueOf(receivedMessage.getFrom().getId()));
        if (telegramUser != null) {
            generateOTP(chatId);
        } else {
            registerUser(chatId);
        }
    }

    /**
     * Delete keyboard with all buttons
     *
     * @param chatId      - target chat id
     * @param messageText - message text
     */
    private void hideKeyboardButtons(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        message.setReplyMarkup(replyKeyboardRemove);
        sendToUser(message);
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
        SendMessage message = new SendMessage(chatId, configTelegram.getRegistrationChatMessage());
        message.setReplyMarkup(prepareRegistrationButton(configTelegram.getRegistrationButtonText(), ButtonType.REQUEST_CONTACT));
        sendToUser(message);
    }

    private void generateOTP(long chatId) {
        SendMessage message = new SendMessage(chatId, configTelegram.getGenerateOTPChatMessage());
        message.setReplyMarkup(prepareRegistrationButton(configTelegram.getGenerateOTPButtonText(), ButtonType.REQUEST_TEXT));
        sendToUser(message);
    }

    private ReplyKeyboardMarkup prepareRegistrationButton(String buttonText, ButtonType buttonType) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(createKeyboardRow(buttonText, buttonType));
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private KeyboardRow createKeyboardRow(String buttonText, ButtonType buttonType) {
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton;
        switch (buttonType) {
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

    public void sendToUser(SendMessage message) {
        try {
            if (message.getChatId() != null && message.getText() != null) {
                execute(message);
                logger.debug("Sent message: {}", message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
        newUser.setUserId(Long.valueOf(contact.getUserID()));
        newUser.setFirstName(contact.getFirstName());
        newUser.setLastName(contact.getLastName());
        newUser.setPhoneNumber(contact.getPhoneNumber());
        newUser.setUserName(sender.getUserName());
        newUser.setBot(sender.getBot());
        newUser.setLanguageCode(sender.getLanguageCode());
        telegramUserService.saveTelegramUserIfNotExist(newUser);
    }

    /**
     * Processes received contact
     *
     * @param contact - received contact
     * @param sender  - sender
     */
    private void processContact(Contact contact, User sender) {
        TelegramUser user = telegramUserService.findTelegramUserByPhoneNumber(contact.getPhoneNumber());
        if (user == null) {
            saveContact(contact, sender);
        }
    }

    enum ButtonType {
        REQUEST_CONTACT("RequestContact"),
        REQUEST_TEXT("RequestText");

        private final String value;

        ButtonType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
