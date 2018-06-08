package de.robinkuck.nodechat.android.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.history.ChatHistory;
import de.robinkuck.nodechat.android.history.GlobalChatHistory;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.history.HistoryMessage;
import de.robinkuck.nodechat.android.json.JSONReaderAndWriter;

public class ChatHistoryManager {

    private static ChatHistoryManager INSTANCE;

    private HashMap<Integer, ChatHistory<? extends HistoryMessage>> chatHistoryMap;

    private JSONReaderAndWriter globalChatReaderAndWriter;

    private ChatHistoryManager() {
        System.out.println("[ChatHistoryManager] starting ChatHistoryManager");
        globalChatReaderAndWriter = new JSONReaderAndWriter("history_global.json");
        chatHistoryMap = new HashMap<>();
    }

    public static ChatHistoryManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatHistoryManager();
        }
        return INSTANCE;
    }

    public ChatHistory<? extends HistoryMessage> getChatHistory(final int chatID) {
        return chatHistoryMap.get(chatID);
    }

    public ChatHistory<GlobalHistoryMessage> getGlobalChatHistory() {
        return (ChatHistory<GlobalHistoryMessage>) getChatHistory(0);
    }

    public void loadData() {
        if (!historyFileExists("history_global.json")) {
            System.out.println("[ChatHistoryManager] creating history_global.json");
            saveData();
        }
        try {
            JSONObject chatObject = globalChatReaderAndWriter.readJSONObject();
            if (chatObject != null && !chatObject.toString().equals("{}")) {
                JSONArray messagesArray = chatObject.getJSONArray("messages");
                int unreadCount = chatObject.getInt("unreadMessagesCount");
                boolean isMuted = chatObject.getBoolean("muted");
                chatHistoryMap.put(0, new GlobalChatHistory(messagesArray, unreadCount, isMuted));
            } else {
                saveData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        if (chatHistoryMap.get(0) == null) {
            chatHistoryMap.put(0, new GlobalChatHistory());
        }
        globalChatReaderAndWriter.writeJSONObject(chatHistoryMap.get(0).toJSONObject());
    }

    private boolean historyFileExists(final String fileName) {
        return App.getInstance().getBaseContext().getFileStreamPath(fileName).exists();
    }

}
