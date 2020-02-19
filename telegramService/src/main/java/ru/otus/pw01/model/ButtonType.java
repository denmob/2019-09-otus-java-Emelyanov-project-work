package ru.otus.pw01.model;

public enum ButtonType {
  REQUEST_CONTACT("RequestContact"),
  REQUEST_TEXT("RequestText");

  private final String value;

  ButtonType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
