package ru.otus.pw.library.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "user_data")
public class UserData implements Serializable {

    private static final long serialVersionUID = 129349938L;

    @Field("phone_number")
    private String phoneNumber;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("user_id")
    private Long userID;

    public UserData(String phoneNumber, String firstName, String lastName, Long userID) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
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

    public String getLastName() {
        return lastName;
    }

    public Long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData that = (UserData) o;
        return userID.equals(that.userID) &&
                firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, firstName, lastName, phoneNumber);
    }
}
