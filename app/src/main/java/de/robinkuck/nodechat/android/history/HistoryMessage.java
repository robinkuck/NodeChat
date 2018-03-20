package de.robinkuck.nodechat.android.history;

import java.io.Serializable;

public abstract class HistoryMessage implements Serializable {

    private boolean personal;
    private String dateString, nameString, messageString;

    public HistoryMessage(final boolean isPersonal, final String dateString,
                          final String nameString, final String messageString) {
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
}
