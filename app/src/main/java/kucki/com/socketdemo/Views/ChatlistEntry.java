package kucki.com.socketdemo.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kucki.com.socketdemo.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kuckr on 23.08.2017.
 *
 *
 *
 */

public class ChatlistEntry extends RelativeLayout {

    public boolean isGlobal;
    public Activity act;

    private String nick;
    private int messages = 0;
    private TextView tvtitle;
    private TextView tvmessagesCount;

    public ChatlistEntry(Context ct, Activity act, String nick) {
        super(act);
        this.act = act;
        init(ct, nick);
    }

    public ChatlistEntry(Context ct, AttributeSet attrs) {
        super(ct,attrs);
    }


    public void init(Context ct, String nick) {
        LayoutInflater inflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatlistentry_view_container, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        tvtitle = (TextView)rl.getChildAt(0);
        tvmessagesCount = (TextView)rl.getChildAt(1);

        if(nick.equals("global")) {
            isGlobal = true;
            this.nick = "Global Chat";
            tvtitle.setText("Global Chat");
        } else {
            isGlobal = false;
            this.nick = nick;
            tvtitle.setText(this.nick);
        }
    }

    public void setMessageCount(int messages) {
        this.messages = messages;
        if(messages>0) {
            tvmessagesCount.setBackgroundResource(R.drawable.messagescountview2);
        } else {
            tvmessagesCount.setBackgroundResource(R.drawable.messagescountview);
        }
        tvmessagesCount.setText(messages);
    }

    public int getMessageCount() {
        return messages;
    }
}