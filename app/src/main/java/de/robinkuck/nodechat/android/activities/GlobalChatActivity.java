package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.history.ChatHistory;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.history.HistoryMessage;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class GlobalChatActivity extends ChatActivity {

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configSocketEvents();
        configSendButton();
        getSupportActionBar().setTitle("Global chat");

        nick = getIntent().getStringExtra("nick");
        ChatlistFragment.getInstance().getGlobalChatlistEntry().resetTVUnreadMessagesCount();
        if (ChatHistoryManager.getInstance().getGlobalChatHistory().getUnreadMessagesCount() > 0) {
            ChatHistoryManager.getInstance().getGlobalChatHistory().resetUnreadMessagesCount();
        }

        scrollToBottom();

        ChatlistFragment.getInstance().getGlobalChatlistEntry().resetTVUnreadMessagesCount();
        if (ChatHistoryManager.getInstance().getGlobalChatHistory().getUnreadMessagesCount() > 0) {
            ChatHistoryManager.getInstance().getGlobalChatHistory().resetUnreadMessagesCount();
        }
        super.ID = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomActivityManager.getInstance().setCurrentActivity(this);
    }

    private void configSocketEvents() {
        SocketManager.getInstance().getSocket().on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                notifyRecylerView();
            }
        });
    }

    private void configSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMsg.getText().toString().trim().equals("")) {

                } else {
                    if (InternetConnectionManager.getInstance().isOnline()) {
                        final String message = editMsg.getText().toString().trim();
                        final String date = GlobalChatActivity.super.getCurrentDateString();
                        clearEditMsg();
                        GlobalHistoryMessage historyMessage = new GlobalHistoryMessage(true, date, "", message);
                        ChatHistoryManager.getInstance().getGlobalChatHistory().addSentMessage(historyMessage);
                        notifyRecylerView();
                        SocketManager.getInstance().sendGlobalMessage(message, date,
                                new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        System.out.println("[I] Global Message was sent successfully!");
                                    }
                                });
                        clearEditMsg();
                    }
                }
            }
        });
    }

    @Override
    public ChatHistory<? extends HistoryMessage> getHistory() {
        return ChatHistoryManager.getInstance().getGlobalChatHistory();
    }
}
