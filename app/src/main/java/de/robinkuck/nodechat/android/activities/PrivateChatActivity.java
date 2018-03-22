package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.utils.UiUtils;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
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

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void configSocketEvents() {
        SocketManager.getInstance().getSocket().on("privatemessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s1 = data.getString("text");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addPrivateMessageView(s1);
                            UiUtils.scrollDown(scroller);
                        }
                    });
                } catch (JSONException e) {
                    System.out.println("Error getting new message!");
                }
            }
        }).on("yourmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s2 = data.getString("text");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiUtils.scrollDown(scroller);
                        }
                    });
                } catch (JSONException e) {
                    System.out.println("Error getting new message!");
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
                        SocketManager.getInstance().sendPrivateMessage(recipient, editMsg.getText().toString());
                        createPersonalMessageView(editMsg.getText().toString());
                        clearEditMsg();
                        UiUtils.scrollDown(scroller);
                    }
                }
            }
        });
    }

    public void addPrivateMessageView(String text) {
        //MessageView mv = super.createMessageView(text, false);
    }

    public String getRecipient() {
        return recipient;
    }

}
