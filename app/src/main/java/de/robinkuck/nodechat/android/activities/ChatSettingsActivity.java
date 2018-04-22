package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.history.ChatHistory;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;

public class ChatSettingsActivity extends AbstractChildActivity {

    private ChatHistory<?> chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatsettings);
        super.configViews();
        final int chatID = getIntent().getIntExtra("id", -1);
        chatHistory = ChatHistoryManager.getInstance().getChatHistory(chatID);
    }

    public void onClearHistory(final View view) {
        chatHistory.clearMessages();
    }
}
