package com.softwaresaturdays.app.fancychat.models;

// Child class of a Message
public class TextMessage extends Message {

    // TODO Add a String data member for a text
    // Text is the only addition

    public TextMessage() {
        super();
    }

    public TextMessage(String text, String author) {
        super(System.currentTimeMillis(), author, Message.TYPE_TEXT_MESSAGE);

        this.text = text;
    }

    // TODO Add a getter for text
}
