package ru.otus.pw.library.secret;

public enum HandShake {
  REGISTRATION_VALUE("RegistrationValue");

  private final String value;

  HandShake(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
