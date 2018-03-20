package de.robinkuck.nodechat.android.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalChatHistory extends ChatHistory<GlobalHistoryMessage> {

    public GlobalChatHistory() {
        super("Global Chat");
    }

    public GlobalChatHistory(final JSONArray messagesList, final int unreadMessagesCount) {
        super("Global Chat");
        System.out.println("[ChatHistoryManager] creating GlobalChatHistory " + messagesList + " " + unreadMessagesCount);
        setUnreadMessagesCount(unreadMessagesCount);
        loadMessages(messagesList);
    }

    @Override
    public void addIncomingMessage(final GlobalHistoryMessage message, final boolean isReading) {
        super.addIncomingMessage(message, isReading);
    }

    @Override
    public void loadMessages(final JSONArray messageList) {
        try {
            for (int i = 0; i < messageList.length(); i++) {
                JSONObject currentMessage = messageList.getJSONObject(i);
                GlobalHistoryMessage message = new GlobalHistoryMessage(currentMessage.getBoolean("personal"),
                        currentMessage.getString("dateString"), currentMessage.getString("nameString"),
                        currentMessage.getString("messageString"));
                loadHistoryMessage(message);
            }
        } catch (JSONException e) {

        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = new JSONObject(mapper.writeValueAsString(this));
        } catch (JSONException e) {

        } catch (JsonProcessingException e) {

        }
        return result;
    }
}
