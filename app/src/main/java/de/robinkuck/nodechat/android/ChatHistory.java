package de.robinkuck.nodechat.android;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatHistory<messageObj extends Message> {

    @JsonProperty("unreadMessagesCount")
    protected int unreadMessagesCount;
    @JsonProperty("chatLabel")
    protected String chatLabel;
    @JsonProperty("messages")
    private List<messageObj> messages;

    public ChatHistory() {
        messages = new ArrayList<>();
    }

    public ChatHistory(final String chatLabel) {
        this();
        this.chatLabel = chatLabel;
    }

    public ChatHistory(@JsonProperty("chatLabel") String chatLabel, @JsonProperty("messages")List<messageObj> messages, @JsonProperty("unreadMessagesCount") int unreadMessagesCount) {
        this.chatLabel = chatLabel;
        this.messages = messages;
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public abstract void loadMessages(final JSONArray jsonObject);

    public abstract JSONObject toJSONObject();

    public void addMessage(final messageObj message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }

    public void setMessages(final List<messageObj> messages) {
        this.messages = messages;
    }

    public List<messageObj> getMessages() {
        return messages;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public String getChatLabel() {
        return chatLabel;
    }

    public void setChatLabel(String chatLabel) {
        this.chatLabel = chatLabel;
    }
}
