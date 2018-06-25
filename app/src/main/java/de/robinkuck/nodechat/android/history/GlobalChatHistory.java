package de.robinkuck.nodechat.android.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.fragments.ChatlistFragment;

public class GlobalChatHistory extends ChatHistory<GlobalChatHistoryMessage> {

    public GlobalChatHistory() {
        super("Global Chat", 0, false);
    }

    public GlobalChatHistory(final JSONArray messagesList, final int unreadMessagesCount, final boolean isMuted) {
        super("Global Chat", unreadMessagesCount, isMuted);
        System.out.println("[ChatHistoryManager] creating GlobalChatHistory " + messagesList + " " + unreadMessagesCount);
        setUnreadMessagesCount(unreadMessagesCount);
        loadMessages(messagesList);
    }

    @Override
    public void addIncomingMessage(final GlobalChatHistoryMessage message, final boolean isReading) {
        super.addIncomingMessage(message, isReading);
        if (ChatlistFragment.getInstance() != null && ChatlistFragment.getInstance().getGlobalChatlistEntry() != null && !isReading) {
            ChatlistFragment.getInstance().getGlobalChatlistEntry().setMessageCount(getUnreadMessagesCount());
        }
    }

    @Override
    public void loadMessages(final JSONArray messageList) {
        try {
            for (int i = 0; i < messageList.length(); i++) {
                JSONObject currentMessage = messageList.getJSONObject(i);
                GlobalChatHistoryMessage message = new GlobalChatHistoryMessage(currentMessage.getBoolean("personal"),
                        currentMessage.getString("dateString"), currentMessage.getString("nameString"),
                        currentMessage.getString("messageString"));
                loadHistoryMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = new JSONObject(mapper.writeValueAsString(this));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
