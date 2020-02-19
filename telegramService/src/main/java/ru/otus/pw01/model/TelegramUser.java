package ru.otus.pw01.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "telegram_user")
public class TelegramUser  {

    @Id
    private String id;

    @Field("phoneNumber")
    private String phoneNumber;
    @Field("firstName")
    private String firstName;
    @Field("lastName")
    private String lastName;
    @Field("userID")
    private Long userID;
    @Field("vCard")
    private String vCard;
    @Field("is_bot")
    private Boolean isBot;
    @Field("username")
    private String userName;
    @Field("language_code")
    private String languageCode;

    public Boolean getBot() {
        return isBot;
    }

    public void setBot(Boolean bot) {
        isBot = bot;
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
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getvCard() {
        return vCard;
    }

    public void setvCard(String vCard) {
        this.vCard = vCard;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                ", userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", isBot=" + isBot +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}