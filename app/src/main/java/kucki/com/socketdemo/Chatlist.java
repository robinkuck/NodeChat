package kucki.com.socketdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class Chatlist extends AppCompatActivity {

    //TODO: Chatlist and Nick input in one Activity with 2 Fragments

    public LinearLayout layout;
    public LinearLayout privateChatList;
    public TextView global_chat;

    public String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        nick = getNick();
        System.out.println("[I] Your name: " + nick);

        configViews();
        configSocketEvents();
        App.getSocket().emit("getPlayers");
    }

    private void configViews() {
        layout = (LinearLayout)findViewById(R.id.chatentrylist);
        privateChatList = (LinearLayout)layout.findViewById(R.id.private_chat_list);
        RelativeLayout rl = (RelativeLayout)layout.findViewById(R.id.global_chat);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("[I] Starting Global Chat");
            }
        });
        global_chat = (TextView)rl.findViewById(R.id.name_view);
        global_chat.setText("Global Chat");
    }

    @Override
    public void onBackPressed() {
        ActivityManager.startNickActivity(this);
    }

    public void configSocketEvents() {
        final Context ct = this;
        final String currentNick = this.nick;
        App.getSocket().on("sendPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray users = (JSONArray) args[0];
                System.out.println("[INFO] Updating Chatlist, Users online: " + users.length());
                clearPrivateChatList();
                try {
                    for(int i = 0; i<users.length(); i++) {
                        JSONObject current = users.getJSONObject(i);
                        final String nick = current.getString("name");
                        if(!nick.equalsIgnoreCase(currentNick)) {
                            final ChatlistEntry entry = new ChatlistEntry(ct, nick);
                            System.out.println("Adding User to Chatlist: " + nick);

                            addViewtoPrivateChatList(entry);
                        }
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearPrivateChatList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatList.removeAllViews();
            }
        });
    }

    private void addViewtoPrivateChatList(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatList.addView(v);
            }
        });
    }

    private String getNick() {
        Bundle extras = getIntent().getExtras();
        return extras.getString("nick");
    }

}
