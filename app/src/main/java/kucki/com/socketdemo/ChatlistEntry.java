package kucki.com.socketdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class ChatlistEntry extends View {

    private String nick;
    private TextView tvtitle;

    public ChatlistEntry(Context ct, String nick) {
        super(ct);
        this.nick = nick;
        init(nick);
    }

    public ChatlistEntry(Context ct, AttributeSet attrs) {
        super(ct,attrs);
    }


    public void init(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                MATCH_PARENT,WRAP_CONTENT);
        params.weight = 0;
        params.setMargins((int)dpToPixel(15),(int)dpToPixel(25),0,0);
        tvtitle = (TextView)findViewById(R.id.name_view);
        tvtitle.setLayoutParams(params);
        tvtitle.setTextColor(Color.BLACK);
        tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        tvtitle.setText(text);
        final String fnick = text;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("[I] " + fnick);
            }
        });
    }

    //DP to Pixel
    private float dpToPixel(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
    }
}