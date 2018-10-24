package de.robinkuck.nodechat.android.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.history.ChatHistory;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.NickManager;
import de.robinkuck.nodechat.android.utils.Utils;

public class ChatlistEntryView extends RelativeLayout {

    public boolean isGlobal;
    public Activity act;

    private String nick;
    private int unreadMessagesCount;
    private TextView tvtitle;
    private TextView tvUnreadMessagesCount;
    private ImageView imgVolume;

    public ChatlistEntryView(Activity act, String nick) {
        super(act);
        this.act = act;
        init(nick);
    }

    public void init(String nick) {
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatlistentry_view, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        tvtitle = (TextView) rl.getChildAt(0);
        tvUnreadMessagesCount = (TextView) rl.getChildAt(1);
        imgVolume = (ImageView) rl.findViewById(R.id.imgvolume);

        if (nick.equals("global")) {
            isGlobal = true;
            this.nick = "Global chat";
            tvtitle.setText("Global chat");
            ChatHistory<GlobalHistoryMessage> globalChatHistory = ChatHistoryManager.getInstance().getGlobalChatHistory();
            setMessageCount(globalChatHistory.getUnreadMessagesCount());
            setVolumeIcon(globalChatHistory.isMuted());
        } else {
            isGlobal = false;
            this.nick = nick;
            tvtitle.setText(this.nick);
        }

        final String receipient = nick;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGlobal) {
                    CustomActivityManager.getInstance().startGlobalChatAcitity(ChatlistFragment.getInstance().getActivity(),
                            NickManager.getInstance().getCurrentNick());
                    System.out.println("[I] Global chat started!");
                } else {
                    CustomActivityManager.getInstance().startPrivateChatActivity(ChatlistFragment.getInstance().getActivity(),
                            receipient);
                    System.out.println("[I] Private chat started!");
                }
            }
        });
    }

    public void setMessageCount(final int messagesCount) {
        System.out.println("[I] updating unreadmessagescount");
        this.unreadMessagesCount = messagesCount;
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messagesCount > 0) {
                    tvUnreadMessagesCount.setBackgroundResource(R.drawable.background_messagecount2);
                } else {
                    tvUnreadMessagesCount.setBackgroundResource(R.drawable.background_messagecount);
                }
                if (messagesCount > 99) {
                    tvUnreadMessagesCount.setText("99+");
                } else {
                    tvUnreadMessagesCount.setText(String.valueOf(messagesCount));
                }
            }
        });

    }

    public int getMessageCount() {
        return unreadMessagesCount;
    }

    public void resetTVUnreadMessagesCount() {
        Utils.waitUntil(300, new Runnable() {
            @Override
            public void run() {
                setMessageCount(0);
                tvUnreadMessagesCount.setBackgroundResource(R.drawable.background_messagecount);
            }
        });
    }

    public void setVolumeIcon(final boolean isMuted) {
        if (isMuted) {
            imgVolume.setVisibility(VISIBLE);
        } else {
            imgVolume.setVisibility(INVISIBLE);
        }
    }
}