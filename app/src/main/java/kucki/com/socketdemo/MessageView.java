package kucki.com.socketdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.LinearLayout;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kuckr on 31.07.2017.
 */

public class MessageView extends LinearLayout {

    public int size;
    public boolean isGlobal;

    public MessageView(Context ct, String message, String name, boolean isGlobal, boolean isPersonal,int size) {
        super(ct);
        this.size = size;
        this.isGlobal = isGlobal;
        init(message,name, isGlobal, isPersonal);
    }

    public void init(String text, String name, boolean isGlobal, boolean isPersonal) {


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                WRAP_CONTENT,WRAP_CONTENT);
        params.weight = 0;
        params.setMargins((int)dpToPixel(5),0,
                (int)dpToPixel(5),
                (int)dpToPixel(2));
        setLayoutParams(params);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        setTypeface(Typeface.MONOSPACE);
        setPadding((int)dpToPixel(6),
                isGlobal ? (int)dpToPixel(18) : (int)dpToPixel(6),
                (int)dpToPixel(12),
                (int)dpToPixel(18));
        setMinWidth((int)dpToPixel(110));
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
