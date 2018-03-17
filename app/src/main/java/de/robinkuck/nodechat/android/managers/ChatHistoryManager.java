package de.robinkuck.nodechat.android.managers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.ChatHistory;
import de.robinkuck.nodechat.android.GlobalChatHistory;
import de.robinkuck.nodechat.android.json.JSONReaderAndWriter;

public class ChatHistoryManager {

    private static ChatHistoryManager INSTANCE;

    private GlobalChatHistory globalChatHistory;

    private JSONReaderAndWriter globalChatReaderAndWriter;

    private ChatHistoryManager() {
        System.out.println("[ChatHistoryManager] starting ChatHistoryManager");
        globalChatReaderAndWriter = new JSONReaderAndWriter("history_global.json");
        if (!historyFileExists("history_global.json")) {
            saveData();
        }
        try {
            JSONObject chatObject = globalChatReaderAndWriter.readJSONObject(App.getInstance());
            if (chatObject != null) {
                JSONArray messagesArray = chatObject.getJSONArray("messages");
                int unreadCount = chatObject.getInt("unreadMessagesCount");
                globalChatHistory = new GlobalChatHistory(messagesArray, unreadCount);
                System.out.println("[ChatHistoryManager] " + chatObject.toString());
                System.out.println("[ChatHistoryManager] " + globalChatHistory.getChatLabel() + ", " + globalChatHistory.getUnreadMessagesCount());
                System.out.println("[ChatHistoryManager] globalChatHistory successfully loaded");
                System.out.println("[ChatHistoryManager] " + globalChatHistory.getMessages().size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ChatHistoryManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatHistoryManager();
        }
        return INSTANCE;
    }

    public ChatHistory getGlobalChatHistory() {
        return globalChatHistory;
    }

    public void saveData() {
        if (globalChatHistory == null) {
            globalChatHistory = new GlobalChatHistory();
        }
        JSONObject globalChatJSONObject = globalChatHistory.toJSONObject();
        System.out.println("[ChatHistoryManager] " + globalChatJSONObject.toString());
        globalChatReaderAndWriter.writeJSONObject(globalChatJSONObject, App.getInstance());
    }

    private boolean historyFileExists(final String fileName) {
        return App.getInstance().getBaseContext().getFileStreamPath(fileName).exists();
    }

}
