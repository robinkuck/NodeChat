package kucki.com.socketdemo.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.ActivitySwitcher;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.SocketManager;
import kucki.com.socketdemo.Views.ChatlistEntry;
import kucki.com.socketdemo.activities.MainActivity;
import kucki.com.socketdemo.R;

public class ChatlistFragment extends Fragment {

    public LinearLayout layout;
    public LinearLayout privateChatListLayout;
    public LinearLayout globalChatLayout;

    public String nick;

    private static ChatlistFragment INSTANCE;

    private HashMap<String, ChatlistEntry> entries;
    private ChatlistEntry gEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        INSTANCE = this;

        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nick = ((MainActivity)getActivity()).getNick();
        System.out.println("[I] Your name: " + nick);

        SocketManager.getInstance().getSocket().emit("getUsers");

        View v = getView();
        if(v!=null) {
            configViews(v);
        }
    }

    public static ChatlistFragment getInstance() {
        return INSTANCE;
    }

    private void configViews(View v) {
        entries = new HashMap<>();
        layout = (LinearLayout)v.findViewById(R.id.chatentrylist);
        privateChatListLayout = (LinearLayout)layout.findViewById(R.id.private_chat_list_layout);
        globalChatLayout = (LinearLayout)layout.findViewById(R.id.global_chat_layout);
        gEntry = new ChatlistEntry(getActivity(),"global");
        addViewtoGlobalChatList(gEntry);
        gEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.startGlobalChatAcitity(getActivity(),nick);
                System.out.println("[I] Global Chat started!");
            }
        });
    }

    public void configSocketEvents() {
        final Context ct = getContext();
        final String currentNick = this.nick;
        SocketManager.getInstance().getSocket().on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //TODO: Implement global message event
                gEntry.setMessageCount(gEntry.getMessageCount()+1);
            }
        }).on("privatemessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //TODO: Implement private message event
            }
        });
    }

    public void addViewtoPrivateChatList(final String nick, final ChatlistEntry chatlistEntry) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateChatListLayout.addView(chatlistEntry);
                entries.put(nick, chatlistEntry);
            }
        });
    }

    public void removeViewFromPrivateChatList(final String nick) {
        final ChatlistEntry chatlistEntry = entries.get(nick);
        if(chatlistEntry!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    privateChatListLayout.removeView(chatlistEntry);
                }
            });
        }
    }

    public void addViewtoGlobalChatList(final View v) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalChatLayout.addView(v);
            }
        });
    }

}
