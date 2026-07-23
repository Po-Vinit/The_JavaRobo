package model;

import java.time.LocalDateTime;

public class Message {

    public enum Sender {
        USER,
        BOT
    }

    private final String content;
    private final Sender sender;
    private final LocalDateTime timestamp;

    public Message(String content, Sender sender) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public Sender getSender() {
        return sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender + ": " + content;
    }
}