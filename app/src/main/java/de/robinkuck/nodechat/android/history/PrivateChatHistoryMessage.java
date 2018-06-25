package de.robinkuck.nodechat.android.history;

public class PrivateChatHistoryMessage extends HistoryMessage {

    public PrivateChatHistoryMessage(final boolean isPersonal, final String dateString, final String messageString) {
        super(isPersonal, dateString, "", messageString);
    }

}
