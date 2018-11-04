package com.softwaresaturdays.app.fancychat.models;

// Child class of a Message
public class TextMessage extends Message {

    // Text is the only addition
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
