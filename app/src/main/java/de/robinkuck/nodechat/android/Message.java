package de.robinkuck.nodechat.android;

import java.io.Serializable;

public abstract class Message implements Serializable{

    private String uuid;
    private boolean personal;
    private String dateString, nameString, messageString;

    public Message(final boolean isPersonal, final String uuid, final String dateString,
                   final String nameString, final String messageString) {
        this.uuid = uuid;
        this.personal = isPersonal;
        this.dateString = dateString;
        this.nameString = nameString;
        this.messageString = messageString;
    }

    public boolean isPersonal() {
        return personal;
    }

    public String getDateString() {
        return dateString;
    }

    public String getNameString() {
        return nameString;
    }

    public String getMessageString() {
        return messageString;
    }

    public String getUUId() {
        return uuid;
    }
}
