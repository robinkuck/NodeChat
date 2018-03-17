package de.robinkuck.nodechat.android;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import de.robinkuck.nodechat.android.json.JSONReaderAndWriter;

public class GlobalChatHistory extends ChatHistory<GlobalMessage> {

    public GlobalChatHistory() {
        super("Global Chat");
    }

    public GlobalChatHistory(final JSONArray messagesList, final int unreadMessagesCount) {
        super("Global Chat");
        setUnreadMessagesCount(unreadMessagesCount);
        loadMessages(messagesList);
    }

    @Override
    public void addMessage(final GlobalMessage message) {
        super.addMessage(message);
    }

    @Override
    public void loadMessages(final JSONArray messageList) {
        try {
            for (int i = 0; i < messageList.length(); i++) {
                JSONObject currentMessage = messageList.getJSONObject(i);
                GlobalMessage message = new GlobalMessage(currentMessage.getBoolean("personal"),
                        currentMessage.getString("dateString"), currentMessage.getString("nameString"),
                        currentMessage.getString("messageString"));
                addMessage(message);
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
