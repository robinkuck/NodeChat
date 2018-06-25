package de.robinkuck.nodechat.android.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrivateChatHistory extends ChatHistory<PrivateChatHistoryMessage> {

    public PrivateChatHistory(final String recipient) {
        super(recipient, 0, false);
    }

    @Override
    public void addIncomingMessage(final PrivateChatHistoryMessage message, final boolean isReading) {
        super.addIncomingMessage(message, isReading);
    }

    @Override
    public void loadMessages(final JSONArray messageList) {
        try {
            for (int i = 0; i < messageList.length(); i++) {
                JSONObject currentMessage = messageList.getJSONObject(i);
                PrivateChatHistoryMessage message = new PrivateChatHistoryMessage(currentMessage.getBoolean("personal"),
                        currentMessage.getString("dateString"), currentMessage.getString("messageString"));
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
