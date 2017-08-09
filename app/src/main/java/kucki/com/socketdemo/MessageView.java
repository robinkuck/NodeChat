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
        params.setMargins(30,0,30,30);
        setLayoutParams(params);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        setTypeface(Typeface.MONOSPACE);
        setPadding(15,15,15,15);
        setMaxWidth(size);
        setBackgroundResource(R.drawable.bgmessageview);
        setText(text);
        //this.setBackgroundDrawable(new BitmapDrawable(rbt));
    }

}
