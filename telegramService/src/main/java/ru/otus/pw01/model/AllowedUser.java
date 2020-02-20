package ru.otus.pw01.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "allowed_user")
public class AllowedUser {

    @Id
    private String id;

    @Field("user_name")
    private String userName;

    @Field("phone_number")
    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "AllowedUser{" +
                "id=" + id +
                ", userName=" + userName +
                ", phoneNumber='" + phoneNumber +'}';
    }

}