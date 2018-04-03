package de.robinkuck.nodechat.android.history;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.views.ChatlistEntry;
import de.robinkuck.nodechat.android.views.MessageView;

public abstract class ChatHistory<messageObj extends HistoryMessage> {

    @JsonProperty("unreadMessagesCount")
    protected int unreadMessagesCount;
    @JsonProperty("chatLabel")
    protected String chatLabel;
    @JsonProperty("messages")
    private List<messageObj> messages;

    private transient List<MessageView> messageViews;

    public ChatHistory() {
        messages = new ArrayList<>();
        messageViews = new ArrayList<>();
    }

    public ChatHistory(final String chatLabel) {
        this();
        this.chatLabel = chatLabel;
    }

    public abstract void loadMessages(final JSONArray jsonObject);

    public abstract JSONObject toJSONObject();

    public void addIncomingMessage(final messageObj message, final boolean isReading) {
        messages.add(message);
        //save data
        incUnreadMessagesCount();
        ChatHistoryManager.getInstance().saveData();
    }

    public void addSentMessage(final messageObj message) {
        messages.add(message);
        ChatHistoryManager.getInstance().saveData();
    }

    public void loadHistoryMessage(final messageObj message) {
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

    public void resetUnreadMessagesCount() {
        setUnreadMessagesCount(0);
        ChatHistoryManager.getInstance().saveData();
    }

    public String getChatLabel() {
        return chatLabel;
    }

    public void setChatLabel(String chatLabel) {
        this.chatLabel = chatLabel;
    }

    private void incUnreadMessagesCount() {
        unreadMessagesCount++;
    }
}
