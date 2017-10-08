package kucki.com.socketdemo.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.R;

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
                            /*
                            messages.setText(s1 + "\n\n" + s2);
                            scrollDown();
                            */
                            addGlobalMessageView(s1, s2);
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
                            /*
                            messages.setText(s1 + "\n\n" + s2);
                            scrollDown();
                            */
                            addPersonalMessageView(msg);
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
                if (App.getSocket().connected() && !editMsg.getText().toString().equals("")) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("msg", editMsg.getText());
                        App.getSocket().emit("new gmsg", data);
                        addPersonalMessageView(editMsg.getText().toString());
                        clearEditMsg();
                        scrollDown();
                        System.out.println("[I] Sending global message!");
                    } catch (JSONException e) {
                        System.out.println("Error sending data");
                    }
                }
            }
        });
    }

    //Messages from the server
    public void addGlobalMessageView(String sender, String text) {
        MessageView mv = super.createMessageView(text, true);
        addNameHeader(mv, sender);
        super.addDate(mv);
    }

    public void addNameHeader(MessageView message, String sender) {
        BadgeView badgeView = new BadgeView(this, message);
        badgeView.setText(sender);
        badgeView.setTextColor(getResources().getColor(R.color.nameHeader));
        badgeView.setTextSize((int) super.dpToPixel(5));
        badgeView.setBadgeBackgroundColor(Color.TRANSPARENT);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
        //badgeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.linemessageview));
        //badgeView.setBadgeMargin((int)dpToPixel(3),(int)dpToPixel(3));
        badgeView.show();
    }

}
