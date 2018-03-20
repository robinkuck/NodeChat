package de.robinkuck.nodechat.android.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.history.GlobalChatHistory;
import de.robinkuck.nodechat.android.json.JSONReaderAndWriter;

public class ChatHistoryManager {

    private static ChatHistoryManager INSTANCE;

    private GlobalChatHistory globalChatHistory;

    private JSONReaderAndWriter globalChatReaderAndWriter;

    private ChatHistoryManager() {
        System.out.println("[ChatHistoryManager] starting ChatHistoryManager");
        globalChatReaderAndWriter = new JSONReaderAndWriter("history_global.json");
    }

    public static ChatHistoryManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatHistoryManager();
        }
        return INSTANCE;
    }

    public GlobalChatHistory getGlobalChatHistory() {
        return globalChatHistory;
    }

    public void loadData() {
        if (!historyFileExists("history_global.json")) {
            System.out.println("[ChatHistoryManager] creating history_global.json");
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
