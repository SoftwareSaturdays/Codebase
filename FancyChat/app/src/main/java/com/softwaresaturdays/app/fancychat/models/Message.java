package com.softwaresaturdays.app.fancychat.models;

public class Message {

    private long timestamp;
    private String author;
    private String type;

    public static String TYPE_TEXT_MESSAGE = "1";
    public static String TYPE_GIF_MESSAGE = "2";

    public Message() {

    }

    public Message(long timestamp, String author, String type) {
        this.timestamp = timestamp;
        this.author = author;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }
}
