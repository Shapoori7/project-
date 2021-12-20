package model;

import java.time.LocalDate;

public class Message {
    private final String sender;
    private final String text;
    private final LocalDate date;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "<" + sender + "> : \"" + text + "\"";
    }
}
