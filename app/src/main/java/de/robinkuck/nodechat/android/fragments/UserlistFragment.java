package de.robinkuck.nodechat.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.managers.SocketManager;
import de.robinkuck.nodechat.android.views.UserEntryView;

public class UserlistFragment extends Fragment {

    private LinearLayout userList;

    private static UserlistFragment INSTANCE;

    private HashMap<String, UserEntryView> entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        INSTANCE = this;
        if (SocketManager.getInstance().getSocket() != null) {
            SocketManager.getInstance().getSocket().emit("getUsers");
        }
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        if (v != null) {
            configViews(v);
        }
    }

    public static UserlistFragment getInstance() {
        return INSTANCE;
    }

    public void clearUserList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userList.removeAllViews();
                entries.clear();
            }
        });
    }

    public void addViewtoUserList(final String nick, final UserEntryView userEntry) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userList.addView(userEntry);
                entries.put(nick, userEntry);
            }
        });
    }

    public void removeViewFromUserList(final String nick) {
        final UserEntryView userEntry = entries.get(nick);
        if (userEntry != null) {
            userList.removeView(userEntry);
        }
    }

    private void configViews(View v) {
        entries = new HashMap<>();
        userList = v.findViewById(R.id.users_list);
    }

}
