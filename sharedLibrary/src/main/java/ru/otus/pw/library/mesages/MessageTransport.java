package ru.otus.pw.library.mesages;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MessageTransport implements Serializable {

  private static final long serialVersionUID = 129348938L;

  private final UUID id = UUID.randomUUID();
  private final String from;

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public CommandType getCommand() {
    return command;
  }

  public Object getObject() {
    return object;
  }

  private final String to;
  private final CommandType command;
  private final Object object;


  public MessageTransport(String from, String to, CommandType command, Object object ) {
    this.from = from;
    this.to = to;
    this.command = command;
    this.object = object;
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
    return "Message{" +
        "id=" + id +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", command=" + command +
        ", object='" + object +
        '}';
  }


}
