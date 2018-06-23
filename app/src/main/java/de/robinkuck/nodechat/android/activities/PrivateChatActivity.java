package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.history.ChatHistory;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class PrivateChatActivity extends ChatActivity {

    private String recipient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRecipient(getIntent().getStringExtra("chatwith"));
        getSupportActionBar().setTitle(recipient);
        configSocketEvents();
        configSendButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomActivityManager.getInstance().setCurrentActivity(this);
    }

    @Override
    public ChatHistory<?> getHistory() {
        return null;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void configSocketEvents() {
        SocketManager.getInstance().getSocket().on("privatemessage", new Emitter.Listener() {
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
                        final String date = PrivateChatActivity.super.getCurrentDateString();
                        clearEditMsg();
                        GlobalHistoryMessage historyMessage = new GlobalHistoryMessage(true, date, "", message);
                        ChatHistoryManager.getInstance().getGlobalChatHistory().addSentMessage(historyMessage);
                        notifyRecylerView();
                        SocketManager.getInstance().sendPrivateMessage(recipient, message, date,
                                new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        System.out.println("[I] Private Message was sent successfully!");
                                    }
                                });
                        clearEditMsg();
                    }
                }
            }
        });
    }

    public String getRecipient() {
        return recipient;
    }

}
