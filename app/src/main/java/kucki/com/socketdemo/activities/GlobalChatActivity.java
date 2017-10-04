package kucki.com.socketdemo.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.R;

/**
 * Created by D070264 on 21.09.2017.
 */

public class GlobalChatActivity extends ChatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configSocketEvents();
    }

    public void configSocketEvents() {
        App.getSocket().on("globalmessage",new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String s1 = data.getString("sender");
                    final String s2 = data.getString("text");
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
                }catch(JSONException e){
                    System.out.println("Error getting new message!");
                }
            }
        }).on("yourmessage",new Emitter.Listener() {
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
                            addPersonalMessageView(s2);
                            scrollDown();
                        }
                    });
                }catch(JSONException e){
                    System.out.println("Error getting new message!");
                }
            }
        });
    }

    //Messages from the server
    public void addGlobalMessageView(String sender, String text) {
        MessageView mv = super.createMessageView(text);
        addNameHeader(mv, sender);
    }

    public void addNameHeader(MessageView message, String sender) {
        BadgeView badgeView = new BadgeView(this, message);
        badgeView.setText(sender);
        badgeView.setTextColor(getResources().getColor(R.color.nameHeader));
        badgeView.setTextSize((int)super.dpToPixel(4));
        badgeView.setBadgeBackgroundColor(Color.TRANSPARENT);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
        //badgeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.linemessageview));
        //badgeView.setBadgeMargin((int)dpToPixel(3),(int)dpToPixel(3));
        badgeView.show();
    }

}
