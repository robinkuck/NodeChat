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
import kucki.com.socketdemo.ActivityManager;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.ChatlistEntry;
import kucki.com.socketdemo.activities.MainActivity;
import kucki.com.socketdemo.R;

public class ChatlistFragment extends Fragment {

    public LinearLayout layout;
    public LinearLayout privateChatListLayout;
    public LinearLayout globalChatLayout;
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

        nick = ((MainActivity)getActivity()).getNick();
        System.out.println("[I] Your name: " + nick);

        configSocketEvents();
        App.getSocket().emit("getPlayers");

        View v = getView();
        if(v!=null) {
            configViews(v);
        }
    }

    private void configViews(View v) {
        layout = (LinearLayout)v.findViewById(R.id.chatentrylist);
        privateChatListLayout = (LinearLayout)layout.findViewById(R.id.private_chat_list_layout);
        globalChatLayout = (LinearLayout)layout.findViewById(R.id.global_chat_layout);
        final ChatlistEntry entry = new ChatlistEntry(getActivity(),"global");
        addViewtoGlobalChatList(entry);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startGlobalChatAcitity(getActivity());
                System.out.println("[I] Global Chat started!");
            }
        });
        /*
        global_chat = (TextView)rl.findViewById(R.id.name_view);
        global_chat.setText("Global Chat");
        */
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
                            final ChatlistEntry entry = new ChatlistEntry(getActivity(), nick);
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
                privateChatListLayout.removeAllViews();
            }
        });
    }

    private void addViewtoPrivateChatList(final View v) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatListLayout.addView(v);
            }
        });
    }

    private void addViewtoGlobalChatList(final View v) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalChatLayout.addView(v);
            }
        });
    }

}
