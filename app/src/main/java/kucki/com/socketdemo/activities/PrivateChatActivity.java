package kucki.com.socketdemo.activities;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;

/**
 * Created by D070264 on 21.09.2017.
 */

public class PrivateChatActivity extends ChatActivity {

    private String sRecipient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecipient(getIntent().getStringExtra("chatwith"));
        configSocketEvents();
    }

    public void setRecipient(String pRecipient) {
        sRecipient = pRecipient;
    }

    public void configSocketEvents() {
        App.getSocket().on("privatemessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s1 = data.getString("text");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*
                            messages.setText(s1 + "\n\n" + s2);
                            scrollDown();
                            */
                            addPrivateMessageView(s1);
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
                    //final String s1 = messages.getText().toString();
                    final String s2 = data.getString("text");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*
                            messages.setText(s1 + "\n\n" + s2);
                            scrollDown();
                            */
                            PrivateChatActivity.super.createPChatMessageView(s2);
                            scrollDown();
                        }
                    });
                } catch (JSONException e) {
                    System.out.println("Error getting new message!");
                }
            }
        });
    }

    public void addPrivateMessageView(String text) {
        //MessageView mv = super.createMessageView(text, false);
    }

    public String getRecipient() {
        return sRecipient;
    }

}
