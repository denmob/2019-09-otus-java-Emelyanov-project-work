package ru.otus.pw.library.mesages;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MessageTransport implements Serializable {

  private static final long serialVersionUID = 129348938L;

  private final UUID id = UUID.randomUUID();
  private final CommandType command;
  private final String data;
  private final String from;
  private final String to;

  public CommandType getCommand() {
    return command;
  }
  public String getData() {
    return data;
  }
  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }


  public MessageTransport(String from, String to, CommandType command, String data) {
    this.from = from;
    this.to = to;
    this.command = command;
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MessageTransport message = (MessageTransport) o;
    return id == message.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Message{id=" + id + ", from=" + from + ", to=" + to + ", command=" + command + ", data='" + data + '}';
  }
}
