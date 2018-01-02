package kucki.com.socketdemo.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.SocketManager;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by KuckR on 21.09.2017.
 */

public class GlobalChatActivity extends ChatActivity {

    public String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInstance().setCurrentActivity(this);

        configSocketEvents();
        configSendButton();
        setTitle("Global Chat");

        nick = getIntent().getStringExtra("nick");
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }

    private void configSocketEvents() {
        SocketManager.getInstance().getSocket().on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s1 = data.getString("from");
                    final String s2 = data.getString("msg");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createGChatMessageView(s2,s1);
                            scrollDown();
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
                    if(App.getInstance().isOnline()) {
                        SocketManager.getInstance().sendGlobalMessage(editMsg.getText().toString());
                        createPersonalMessageView(editMsg.getText().toString());
                        clearEditMsg();
                        scrollDown();
                    }
                }
            }
        });
    }

    private void createGChatMessageView(String msg, String name) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);

        MessageView mv = new MessageView(this, msg, name, true, false, ((x / 3) * 2));
        l.addView(mv);
        msgs.addView(l);
        System.out.println("[I] GChatMessageView created!");
    }
}
