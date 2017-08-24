package kucki.com.socketdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kuckr on 23.08.2017.
 */

public class ChatlistEntry extends android.support.v7.widget.AppCompatTextView {

    private String nick;

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
        setLayoutParams(params);
        setTextColor(Color.BLACK);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        setText(text);
        //this.setBackgroundDrawable(new BitmapDrawable(rbt));
    }

    //DP to Pixel
    private float dpToPixel(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
    }
}
