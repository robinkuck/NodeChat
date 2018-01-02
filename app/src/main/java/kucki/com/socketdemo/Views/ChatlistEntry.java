package kucki.com.socketdemo.Views;

import  android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kucki.com.socketdemo.R;

/**
 * Created by kuckr on 23.08.2017.
 */

public class ChatlistEntry extends RelativeLayout {

    public boolean isGlobal;
    public Activity act;

    private String nick;
    private int messages = 0;
    private TextView tvtitle;
    private TextView tvmessagesCount;

    public ChatlistEntry(Activity act, String nick) {
        super(act);
        this.act = act;
        init(nick);
    }

    public void init(String nick) {
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatlistentry_view, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        tvtitle = (TextView) rl.getChildAt(0);
        tvmessagesCount = (TextView) rl.getChildAt(1);

        if (nick.equals("global")) {
            isGlobal = true;
            this.nick = "Global Chat";
            tvtitle.setText("Global Chat");
        } else {
            isGlobal = false;
            this.nick = nick;
            tvtitle.setText(this.nick);
        }
    }

    public void setMessageCount(final int messages) {
        this.messages = messages;

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messages > 0) {
                    tvmessagesCount.setBackgroundResource(R.drawable.messagescountview2);
                } else {
                    tvmessagesCount.setBackgroundResource(R.drawable.messagescountview);
                }
                if(messages>99) {
                    tvmessagesCount.setText("99+");
                } else {
                    tvmessagesCount.setText(String.valueOf(messages));
                }
            }
        });

    }

    public int getMessageCount() {
        return messages;
    }
}