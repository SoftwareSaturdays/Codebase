package com.softwaresaturdays.app.fancychat.models;

public class TextMessage extends Message {
    private String text;

    public TextMessage() {
        super();
    }

    public TextMessage(String text, String author) {
        super(System.currentTimeMillis(), author, Message.TYPE_TEXT_MESSAGE);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
