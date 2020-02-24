package ru.otus.pw.library.mesages;

public enum CommandType {
  GENERATE_OTP("GenerateOTP");

  private final String value;

  CommandType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
