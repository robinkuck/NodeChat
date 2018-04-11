package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
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

        super.adapter = new ListViewAdapter(ChatHistoryManager.getInstance().getGlobalChatHistory().getMessages());
        super.recyclerView.setAdapter(super.adapter);
        scrollToBottom();

        ChatlistFragment.getInstance().getGlobalChatlistEntry().resetTVUnreadMessagesCount();
        if (ChatHistoryManager.getInstance().getGlobalChatHistory().getUnreadMessagesCount() > 0) {
            ChatHistoryManager.getInstance().getGlobalChatHistory().resetUnreadMessagesCount();
        }
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
                JSONObject data = (JSONObject) args[0];
                try {
                    final String from = data.getString("from");
                    final String message = data.getString("msg");
                    final String date = data.getString("date");

                    GlobalHistoryMessage historyMessage = new GlobalHistoryMessage(false, date, from, message);
                    ChatHistoryManager.getInstance().getGlobalChatHistory().addIncomingMessage(
                            historyMessage, false);
                    addMessage();
                    GlobalChatActivity.super.scrollToBottom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        addMessage();
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
}
