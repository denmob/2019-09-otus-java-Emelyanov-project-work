package ru.otus.pw.library.message;

public enum CommandType {
    GENERATE_OTP("GenerateOTP"),
    SUCCESS_GENERATE_OTP("SuccessGeneratedOTP"),
    SAVE_USER_DATA("SaveUserData"),
    SUCCESS_SAVE_USER_DATA("SuccessSaveUserData"),
    RESPONSE_WITH_ERROR("ResponseWithError");

    private final String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
