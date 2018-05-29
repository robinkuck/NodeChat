package de.robinkuck.nodechat.android.history;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.robinkuck.nodechat.android.managers.ChatHistoryManager;

public abstract class ChatHistory<messageObj extends HistoryMessage> implements Serializable {

    @JsonProperty("unreadMessagesCount")
    protected int unreadMessagesCount;
    @JsonProperty("chatLabel")
    protected String chatLabel;
    @JsonProperty
    protected boolean muted;
    @JsonProperty("messages")
    private List<messageObj> messages;

    public ChatHistory() {
        messages = new ArrayList<>();
    }

    public ChatHistory(final String chatLabel, final int unreadMessagesCount, final boolean muted) {
        this();
        this.chatLabel = chatLabel;
        this.muted = muted;
        setUnreadMessagesCount(unreadMessagesCount);

    }

    public abstract void loadMessages(final JSONArray jsonObject);

    public abstract JSONObject toJSONObject();

    public void addIncomingMessage(final messageObj message, final boolean isReading) {
        messages.add(message);
        if (!isReading) {
            incUnreadMessagesCount();
        }
        save();
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
        save();
    }

    public void setMessages(final List<messageObj> messages) {
        this.messages = messages;
        save();
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

    public void muteOrUnmute() {
        if (isMuted()) {
            muted = false;
        } else {
            muted = true;
        }
        save();
    }

    public boolean isMuted() {
        return muted;
    }

    private void incUnreadMessagesCount() {
        unreadMessagesCount++;
    }

    private void save() {
        ChatHistoryManager.getInstance().saveData();
    }
}
