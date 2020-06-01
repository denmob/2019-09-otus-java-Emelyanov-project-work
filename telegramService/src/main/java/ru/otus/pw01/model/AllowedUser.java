package ru.otus.pw01.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "allowed_user")
public class AllowedUser {

    @Id
    private String id;

    @Field("user_name")
    private String userName;

    @Field("phone_number")
    private String phoneNumber;

    @Override
    public String toString() {
        return "AllowedUser{" +
                "id=" + id +
                ", userName=" + userName +
                ", phoneNumber='" + phoneNumber + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllowedUser that = (AllowedUser) o;
        return id.equals(that.id) &&
                userName.equals(that.userName) &&
                phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, phoneNumber);
    }
}
