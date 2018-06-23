package de.robinkuck.nodechat.android;

import junit.framework.Assert;
import org.junit.Test;

import de.robinkuck.nodechat.android.history.GlobalChatHistory;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;

public class GlobalChatHistoryTest {

    @Test
    public void addHistoryMessageTest() {
        GlobalChatHistory history = new GlobalChatHistory();
        history.addIncomingMessage(
                new GlobalHistoryMessage(true, "", "", ""),
                false
        );
        Assert.assertEquals(1, history.getMessages().size());
    }

    /*
    @Test
    public void loadMessagesTest() {
        GlobalChatHistory history = new GlobalChatHistory();
        try {
            JSONArray array = new JSONArray();
            array.put(0, "{\"dateString\":\"24.05.2018, 20:21\",\"messageString\":\"abcde\",\"nameString\":\"\",\"personal\":true}");
            array.put(1, "{\"dateString\":\"24.05.2018, 20:21\",\"messageString\":\"abcde\",\"nameString\":\"\",\"personal\":true}");
            history.loadMessages(array);
            Assert.assertEquals(2, history.getMessages().size());
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    */


}
