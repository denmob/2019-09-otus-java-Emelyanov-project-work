package ru.otus.pw.library.handshake;

public enum HandShake {
  REGISTRATION_VALUE("RegistrationValue"),
  REGISTRATION_SUCCESS("RegistrationSuccess"),
  REGISTRATION_FAIL("RegistrationFail");

  private final String value;

  HandShake(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
