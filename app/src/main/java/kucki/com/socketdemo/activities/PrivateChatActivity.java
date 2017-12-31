package kucki.com.socketdemo.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.SocketManager;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by D070264 on 21.09.2017.
 */

public class PrivateChatActivity extends ChatActivity {

    private String sRecipient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInstance().setCurrentActivity(this);

        setRecipient(getIntent().getStringExtra("chatwith"));
        configSocketEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }

    public void setRecipient(String pRecipient) {
        sRecipient = pRecipient;
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
                            createPChatMessageView(s2);
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

    private void createPChatMessageView(String msg) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);

        MessageView mv = new MessageView(this, msg, "", false, false, ((x / 3) * 2));
        l.addView(mv);
        msgs.addView(l);
    }

}
