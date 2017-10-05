package kucki.com.socketdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private String nick;
    private TextView tvtitle;

    public Activity act;

    public ChatlistEntry(Activity act, String nick) {
        super(act);
        this.act = act;
        init(nick);
    }

    public ChatlistEntry(Context ct, AttributeSet attrs) {
        super(ct,attrs);
    }


    public void init(String nick) {
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT,(int)dpToPixel(80)));
        setBackgroundDrawable(getResources().getDrawable(R.drawable.bgchatlistentry));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                MATCH_PARENT,WRAP_CONTENT);
        params.setMargins((int)dpToPixel(15),(int)dpToPixel(25),0,0);
        tvtitle = new TextView(getContext());
        tvtitle.setLayoutParams(params);
        tvtitle.setTextColor(Color.BLACK);
        tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        addView(tvtitle);

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

    //DP to Pixel
    private float dpToPixel(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
        /*
        final float density = getResources().getDisplayMetrics().density;
        return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
        */
    }
}