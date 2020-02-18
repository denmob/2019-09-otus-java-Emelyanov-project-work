package ru.otus.pw01.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "telegram_user")
public class TelegramUser {

    @Id
    private String id;

    @Field("telegram_user_id")
    private Long telegramUserId;
    @Field("first_name")
    private String firstName;
    @Field("is_bot")
    private Boolean isBot;
    @Field("last_name")
    private String lastName;
    @Field("username")
    private String userName;
    @Field("language_code")
    private String languageCode;
    @Field("phone_number")
    private String phoneNumber;

    public TelegramUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getBot() {
        return isBot;
    }

    public void setBot(Boolean bot) {
        isBot = bot;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.replace("+","");
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                ", telegramUserId=" + telegramUserId +
                ", firstName='" + firstName + '\'' +
                ", isBot=" + isBot +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}