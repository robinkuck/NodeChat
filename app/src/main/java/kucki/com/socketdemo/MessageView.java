package kucki.com.socketdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

        init(ct);
    }

    public MessageView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        init(ct);
    }

    public void init(Context ct) {
        LayoutInflater inflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.message_view_container, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        TvMessage = (TextView) rl.getChildAt(0);
        TvName = (TextView) rl.getChildAt(1);
        TvDate = (TextView) rl.getChildAt(2);

        if(isGlobal&&!isPersonal) {
            addName();
        } else {
            TvName.setText("");
        }

        createMessageText();
        addDate();
    }


    private void createMessageText() {
        TvMessage.setText(this.message);
        TvMessage.setMaxWidth(size);
        TvMessage.setBackgroundResource(
                isPersonal ? R.drawable.bgpersonalmessageview : R.drawable.bgmessageview);
        TvMessage.setPadding((int) dpToPixel(6),
                isPersonal ? (int) dpToPixel(6) : (isGlobal ? (int) dpToPixel(17) : (int) dpToPixel(6)),
                (int) dpToPixel(12),
                (int) dpToPixel(18));
    }

    private void addDate() {
        TvDate.setText(getCurrentTime());
    }

    private void addName() {
        TvName.setText(this.name);
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
