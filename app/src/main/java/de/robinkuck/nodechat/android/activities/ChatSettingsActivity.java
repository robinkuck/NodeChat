package de.robinkuck.nodechat.android.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import de.robinkuck.nodechat.android.ConfirmationDialog;
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
        new ConfirmationDialog(
                this,
                getResources().getString(R.string.confirm_clear_chat_title),
                getResources().getString(R.string.confirm_clear_chat_msg),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatHistory.clearMessages();
                    }
                }, null)
                .show();
    }
}
