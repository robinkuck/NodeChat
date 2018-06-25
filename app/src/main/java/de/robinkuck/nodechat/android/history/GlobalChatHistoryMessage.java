package de.robinkuck.nodechat.android.history;

public class GlobalChatHistoryMessage extends HistoryMessage {

    public GlobalChatHistoryMessage(final boolean personal, final String dateString,
                                    final String nameString, final String messageString) {
        super(personal, dateString, nameString, messageString);
    }

}
