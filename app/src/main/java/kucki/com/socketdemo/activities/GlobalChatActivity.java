package kucki.com.socketdemo.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;

/**
 * Created by KuckR on 21.09.2017.
 */

public class GlobalChatActivity extends ChatActivity {

    public String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configSocketEvents();
        configSendButton();
        setTitle("Global Chat");

        nick = getIntent().getStringExtra("nick");
    }

    public void configSocketEvents() {
        App.getSocket().on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s1 = data.getString("from");
                    final String s2 = data.getString("msg");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlobalChatActivity.super.createGChatMessageView(s2,s1);
                            scrollDown();
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
                    final String msg = data.getString("msg");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlobalChatActivity.super.createPersonalMessageView(msg);
                            scrollDown();
                            System.out.println("[I] !!!");
                        }
                    });
                } catch (JSONException e) {
                    System.out.println("Error getting new message!");
                }
            }
        });
    }

    public void configSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMsg.getText().toString().trim().equals("")) {

                } else {
                    if (App.getSocket().connected() && !editMsg.getText().toString().equals("")) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("msg", editMsg.getText().toString().trim());
                            App.getSocket().emit("new gmsg", data);
                            GlobalChatActivity.super.createPersonalMessageView(editMsg.getText().toString());
                            clearEditMsg();
                            scrollDown();
                            System.out.println("[I] Sending global message!");
                        } catch (JSONException e) {
                            System.out.println("Error sending data");
                        }
                    }
                }
            }
        });
    }
}
