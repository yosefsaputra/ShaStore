package com.example.yosef.shastore.model.connectors;

public abstract class HashMachine {
    protected String text;
    protected String hashedText;

    public HashMachine() {
    }

    public HashMachine(String text) {
        this.text = text;
    }

    public abstract boolean execute();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHashedText() {
        return hashedText;
    }
}
