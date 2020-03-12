package ru.otus.pw01.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "telegram_user")
public class TelegramUser  {

    @Id
    private String id;

    @Field("phone_number")
    private String phoneNumber;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("user_id")
    private Long userId;

    @Field("is_bot")
    private Boolean isBot;

    @Field("user_name")
    private String userName;

    @Field("language_code")
    private String languageCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Long getUserId() {
        return userId;
    }
    public void setBot(Boolean bot) {
        isBot = bot;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                ", userID=" + userId +
                ", firstName='" + firstName + '\'' +
                ", isBot=" + isBot +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramUser that = (TelegramUser) o;
        return id.equals(that.id) &&
                userId.equals(that.userId) &&
                isBot.equals(that.isBot) &&
                firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                languageCode.equals(that.languageCode) &&
                phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,userId, isBot, firstName, lastName,languageCode,phoneNumber);
    }
}