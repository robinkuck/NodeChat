package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.UiUtils;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
import de.robinkuck.nodechat.android.views.GlobalChatForeignMessageView;
import de.robinkuck.nodechat.android.views.OwnMessageView;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

import static android.widget.LinearLayout.HORIZONTAL;

public class GlobalChatActivity extends ChatActivity {

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configSocketEvents();
        configSendButton();
        getSupportActionBar().setTitle("Global chat");

        nick = getIntent().getStringExtra("nick");
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createGChatMessageView(message, from);
                            UiUtils.scrollDown(scroller);
                        }
                    });
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
                        final OwnMessageView messageView = createPersonalMessageView(editMsg.getText().toString());
                        SocketManager.getInstance().sendGlobalMessage(messageView.getMessage(), messageView.getDate(),
                                new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        System.out.println("[I] Message was sent successfully");
                                        messageView.setSuccessfullSent(true);
                                    }
                                });
                        clearEditMsg();
                        UiUtils.scrollDown(scroller);
                    }
                }
            }
        });
    }

    private void createGChatMessageView(String msg, String name) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);

        GlobalChatForeignMessageView mv = new GlobalChatForeignMessageView(this, msg, name, ((x / 3) * 2));
        l.addView(mv);
        msgs.addView(l);
        System.out.println("[I] GChatMessageView created!");
    }
}
