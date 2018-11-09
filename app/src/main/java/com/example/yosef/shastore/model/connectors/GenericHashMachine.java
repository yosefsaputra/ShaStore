package com.example.yosef.shastore.model.connectors;

/**
 * How to use this machine:
 * HashMachine hm = new GenericHashMachine();
 * hm.setText("mypassword");
 * boolean res = hm.execute();
 * String hashedText, salt;
 * if (res) {
 * hashedText = hm.getHashedText();
 * salt = hm.getSalt();
 * }
 */

public class GenericHashMachine extends HashMachine {
    private String salt;

    public GenericHashMachine() {
        super();
    }

    public GenericHashMachine(String text) {
        super(text);
    }

    @Override
    public boolean execute() {
        // TODO: implement the hashing algorithm here
        hashedText = text;
        return false;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
