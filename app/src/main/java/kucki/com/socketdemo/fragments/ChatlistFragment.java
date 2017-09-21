package kucki.com.socketdemo.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.ChatlistEntry;
import kucki.com.socketdemo.activities.MainActivity;
import kucki.com.socketdemo.R;

public class ChatlistFragment extends Fragment {

    public LinearLayout layout;
    public LinearLayout privateChatList;
    public TextView global_chat;

    public String nick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        if(v!=null) {
            configViews(v);
        }

        nick = ((MainActivity)getActivity()).getNick();
        System.out.println("[I] Your name: " + nick);

        configSocketEvents();
        App.getSocket().emit("getPlayers");
    }

    private void configViews(View v) {
        layout = (LinearLayout)v.findViewById(R.id.chatentrylist);
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

    public void configSocketEvents() {
        final Context ct = getContext();
        final String currentNick = this.nick;
        App.getSocket().on("sendPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray users = (JSONArray) args[0];
                System.out.println("[INFO] Updating Users online: " + users.length());
                clearPrivateChatList();
                try {
                    for(int i = 0; i<users.length(); i++) {
                        JSONObject current = users.getJSONObject(i);
                        final String nick = current.getString("name");
                        if(!nick.equalsIgnoreCase(currentNick)) {
                            final ChatlistEntry entry = new ChatlistEntry(ct, nick);
                            System.out.println("Adding User to list: " + nick);

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatList.removeAllViews();
            }
        });
    }

    private void addViewtoPrivateChatList(final View v) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatList.addView(v);
            }
        });
    }

}
