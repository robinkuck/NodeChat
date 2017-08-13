package kucki.com.socketdemo;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kuckr on 31.07.2017.
 */

public class MessageView extends android.support.v7.widget.AppCompatTextView {

    public int size;

    public MessageView(Context ct, String text, int size) {
        super(ct);
        this.size = size;
        init(text);
    }

    public void init(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                WRAP_CONTENT,WRAP_CONTENT);
        params.setMargins((int)dpToPixel(5),0,
                (int)dpToPixel(5),
                (int)dpToPixel(2));
        params.weight = 0;
        setLayoutParams(params);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        setTypeface(Typeface.MONOSPACE);
        setPadding((int)dpToPixel(6),
                (int)dpToPixel(6),
                (int)dpToPixel(12),
                (int)dpToPixel(22));
        setMinWidth((int)dpToPixel(70));
        setMaxWidth(size);
        setBackgroundResource(R.drawable.bgmessageview);
        setTextColor(Color.BLACK);
        setText(text);
        //this.setBackgroundDrawable(new BitmapDrawable(rbt));
    }

    //DP to Pixel
    private float dpToPixel(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        //return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
        return dp * density + 0.5f;
    }
}
