package de.robinkuck.nodechat.android;

public class GlobalMessage extends Message {

    public GlobalMessage(final String uuid, final boolean personal, final String dateString,
                         final String nameString, final String messageString) {
        super(personal, uuid, dateString, nameString, messageString);
    }

}
