package de.robinkuck.nodechat.android;

import junit.framework.Assert;

import org.junit.Test;

import de.robinkuck.nodechat.android.history.GlobalChatHistory;
import de.robinkuck.nodechat.android.history.GlobalChatHistoryMessage;

public class GlobalChatHistoryTest {

    @Test
    public void addHistoryMessageTest() {
        GlobalChatHistory history = new GlobalChatHistory();
        history.addIncomingMessage(
                new GlobalChatHistoryMessage(true, "", "", ""),
                false
        );
        Assert.assertEquals(1, history.getMessages().size());
    }

}
