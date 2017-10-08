package kucki.com.socketdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kuckr on 31.07.2017.
 */

public class MessageView extends RelativeLayout {

    private TextView TvMessage;
    private TextView TvName;
    private TextView TvDate;

    private String message;
    private String name;
    private boolean isGlobal;
    private boolean isPersonal;
    private int size;

    public MessageView(Context ct, String message, String name, boolean isGlobal, boolean isPersonal, int size) {
        super(ct);
        this.size = size;
        this.isGlobal = isGlobal;
        this.isPersonal = isPersonal;
        this.message = message;
        this.name = name;
        init();
    }

    public MessageView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
    }

    public void init() {
        createMessageText(getContext(), message);
        addDate(getContext());
        addName(getContext());
        /*
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
        */
        //this.setBackgroundDrawable(new BitmapDrawable(rbt));
    }

    private void createMessageText(Context ct, String msg) {
        TvMessage = new TextView(ct);

        TvMessage.setMaxWidth(size);
        LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins((int) dpToPixel(5), 0, (int) dpToPixel(5), (int) dpToPixel(5));
        TvMessage.setLayoutParams(params);
        TvMessage.setBackgroundResource(
                isPersonal ? R.drawable.bgpersonalmessageview : R.drawable.bgmessageview);
        TvMessage.setMinWidth((int) dpToPixel(110));
        TvMessage.setPadding((int) dpToPixel(6),
                isPersonal ? (int) dpToPixel(6) : (isGlobal ? (int) dpToPixel(17) : (int) dpToPixel(6)),
                (int) dpToPixel(12),
                (int) dpToPixel(18));
        TvMessage.setText(msg);
        TvMessage.setTextColor(Color.BLACK);
        TvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        TvMessage.setTypeface(Typeface.MONOSPACE);
        TvMessage.setId(R.id.vmessage);
        addView(TvMessage, 0);
    }

    private void addDate(Context ct) {
        TvDate = new TextView(ct);

        LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, (int) dpToPixel(4), (int) dpToPixel(4));
        params.addRule(ALIGN_BOTTOM, R.id.vmessage);
        params.addRule(ALIGN_END, R.id.vmessage);
        params.addRule(ALIGN_RIGHT, R.id.vmessage);
        TvDate.setLayoutParams(params);
        TvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        TvDate.setTextColor(getResources().getColor(R.color.date));
        TvDate.setId(R.id.vdate);
        TvDate.setText(getCurrentTime());
        addView(TvDate, 1);
    }

    private void addName(Context ct) {
        TvName = new TextView(ct);
        LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins((int) dpToPixel(10), 0, 0, 0);
        TvName.setLayoutParams(params);
        TvName.setTextColor(getResources().getColor(R.color.nameHeader));
        TvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        TvName.setText(name);
        TvName.setTypeface(null,Typeface.BOLD);
        addView(TvName, 2);
    }

    public String getCurrentTime() {
        Calendar c = Calendar.getInstance(Locale.GERMANY);
        String date = "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            date = "0" + c.get(Calendar.DAY_OF_MONTH) + "." +
                    (c.get(Calendar.MONTH) < 10 ? "0" + c.get(Calendar.MONTH) : c.get(Calendar.MONTH)) +
                    "." + c.get(Calendar.YEAR);
        } else {
            date = c.get(Calendar.DAY_OF_MONTH) + "." +
                    (c.get(Calendar.MONTH) < 10 ? "0" + c.get(Calendar.MONTH) : c.get(Calendar.MONTH)) +
                    "." + c.get(Calendar.YEAR);
        }

        String time = "";
        if (c.get(Calendar.MINUTE) < 10) {
            time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
        } else {
            time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        }
        return date + ", " + time;
    }

    //DP to Pixel
    private float dpToPixel(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        //return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
        return dp * density + 0.5f;
    }
}
