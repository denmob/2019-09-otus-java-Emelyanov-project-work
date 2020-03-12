package ru.otus.pw.library.message;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MessageTransport implements Serializable {

  private static final long serialVersionUID = 129348938L;

  private final UUID id = UUID.randomUUID();
  private final CommandType command;
  private Object data;
  private final String from;
  private final String to;

  public CommandType getCommand() {
    return command;
  }
  public void setData(Object data) {
    this.data = data;
  }
  public Object getData() {
    return data;
  }
  public String getFrom() {
    return from;
  }
  public String getTo() {
    return to;
  }

  public MessageTransport(String from, String to, CommandType command) {
    this.from = from;
    this.to = to;
    this.command = command;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MessageTransport that = (MessageTransport) o;
     return  id == that.id  &&
             from.equals(that.from) &&
             to.equals(that.to) &&
             command.equals(that.command) &&
             data.equals(that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id,data);
  }

  @Override
  public String toString() {
    return "Message{id=" + id + ", from=" + from + ", to=" + to + ", command=" + command + ", data='" + data + '}';
  }
}
