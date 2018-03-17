package de.robinkuck.nodechat.android;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.views.ChatlistEntry;

public abstract class ChatHistory<messageObj extends Message> {

    @JsonProperty("unreadMessagesCount")
    protected int unreadMessagesCount;
    @JsonProperty("chatLabel")
    protected String chatLabel;
    @JsonProperty("messages")
    private List<messageObj> messages;

    private transient ChatlistEntry chatlistEntry;

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
        incUnreadMessagesCount();
        if (chatlistEntry != null) {
            chatlistEntry.setMessageCount(getUnreadMessagesCount());
        }
        //save data
        ChatHistoryManager.getInstance().saveData();
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

    public void setChatlistEntry(final ChatlistEntry chatlistEntry) {
        this.chatlistEntry = chatlistEntry;
        chatlistEntry.setMessageCount(getUnreadMessagesCount());
    }

    private void incUnreadMessagesCount() {
        unreadMessagesCount++;
    }
}
