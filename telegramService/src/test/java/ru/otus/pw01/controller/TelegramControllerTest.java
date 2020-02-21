package ru.otus.pw01.controller;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;


import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TelegramControllerTest  extends RegTelegramApi{

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private final  static long DEV_CHAT_ID = 475757602L;
    private final static String DEV_ALLOW_USER_PHONE_NUMBER_SET = "80937188891";


    @Autowired
    private TelegramController telegramController;


    @Test
    public void sendTestMessage() {
        SendMessage message = new SendMessage(DEV_CHAT_ID, "test");
        telegramController.sendToUser(message);
    }

    @Test
    @DisplayName("Show registration button")
    public  void onUpdateReceivedRegistrationButton() {
        Update update = mock(Update.class);
        Message receivedMessage = mock(Message.class);
        given(receivedMessage.getText()).willReturn(telegramController.configTelegram.getRegistrationButtonText());
        given(receivedMessage.getChatId()).willReturn(DEV_CHAT_ID);
        given(update.getMessage()).willReturn(receivedMessage);

        telegramController.onUpdateReceived(update);
    }

    @Test
    @DisplayName("Send to user message wait OTP")
    public  void onUpdateReceivedMessageWaitOTP() {
        Update update = mock(Update.class);
        Message receivedMessage = mock(Message.class);
        given(receivedMessage.getText()).willReturn(telegramController.configTelegram.getGenerateOTPButtonText());
        given(receivedMessage.getChatId()).willReturn(DEV_CHAT_ID);
        given(update.getMessage()).willReturn(receivedMessage);

        telegramController.onUpdateReceived(update);
    }

    @Test
    @DisplayName("Send to user message not allowed for registration")
    public  void onUpdateReceivedWithContactNotAllowRegistration() {
        Update update = mock(Update.class);
        Message receivedMessage = mock(Message.class);
        Contact contact = mock(Contact.class);

        given(receivedMessage.getContact()).willReturn(contact);
        given(receivedMessage.getChatId()).willReturn(DEV_CHAT_ID);
        given(update.getMessage()).willReturn(receivedMessage);

        telegramController.onUpdateReceived(update);
    }

    @Test
    @DisplayName("Send to user message press a button to generate the next OTP")
    public  void onUpdateReceivedWithContactAllowRegistration() {
        Update update = mock(Update.class);
        Message receivedMessage = mock(Message.class);
        Contact contact = mock(Contact.class);
        User user =   mock(User.class);

        given(contact.getPhoneNumber()).willReturn(DEV_ALLOW_USER_PHONE_NUMBER_SET);
        given(receivedMessage.getFrom()).willReturn(user);
        given(receivedMessage.getContact()).willReturn(contact);
        given(receivedMessage.getChatId()).willReturn(DEV_CHAT_ID);
        given(update.getMessage()).willReturn(receivedMessage);

        telegramController.onUpdateReceived(update);
    }

    @Test
    @DisplayName("Send to user message wait OTP(if user registered) or need send contact(if user not registered)")
    public  void onUpdateReceivedRandomUserText() {
        Update update = mock(Update.class);
        Message receivedMessage = mock(Message.class);
        User user =   mock(User.class);

        given(receivedMessage.getFrom()).willReturn(user);
        given(receivedMessage.getText()).willReturn("435435435");
        given(receivedMessage.getChatId()).willReturn(DEV_CHAT_ID);
        given(update.getMessage()).willReturn(receivedMessage);

        telegramController.onUpdateReceived(update);
    }

}
