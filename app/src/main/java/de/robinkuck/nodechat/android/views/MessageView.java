package de.robinkuck.nodechat.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import de.robinkuck.nodechat.android.R;

public class MessageView extends RelativeLayout {

    private TextView TvMessage;
    private TextView TvName;
    private TextView TvDate;

    private String message;
    private String date;
    private int size;

    private boolean isSent;

    public MessageView(final Context ct, final String message, final int size) {
        super(ct);
        this.size = size;
        this.message = message;
        this.isSent = false;
        init(ct, true);
    }

    public MessageView(final Context ct, final String message, final String date, final int size) {
        super(ct);
        this.size = size;
        this.message = message;
        this.date = date;
        this.isSent = false;
        init(ct,false);
    }


    public MessageView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        init(ct, false);
    }

    protected void init(Context ct, final boolean createDate) {
        LayoutInflater inflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.message_view_container, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        TvMessage = (TextView) rl.getChildAt(0);
        TvName = (TextView) rl.getChildAt(1);
        TvDate = (TextView) rl.getChildAt(2);
        getTvName().setText("");
        if(createDate) {
            addDate();
        } else {
            setDate(this.date);
        }
    }

    public void setSuccessfullSent(final boolean isSent) {
        this.isSent = isSent;
        if (isSent) {
            //TODO add sent checkmark
        } else {

        }
    }

    public boolean isMessageSent() {
        return isSent;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String dateString) {
        date = dateString;
        getTvDate().setText(date);
    }

    protected TextView getTvMessage() {
        return TvMessage;
    }

    protected TextView getTvName() {
        return TvName;
    }

    protected TextView getTvDate() {
        return TvDate;
    }

    protected void createMessageText() {
        TvMessage.setText(this.message);
        TvMessage.setMaxWidth(size);
        /*
        TvMessage.setBackgroundResource(
                isPersonal ? R.drawable.bgpersonalmessageview : R.drawable.background_messageview);
        TvMessage.setPadding((int) dpToPixel(6),
                isPersonal ? (int) dpToPixel(6) : (isGlobal ? (int) dpToPixel(17) : (int) dpToPixel(6)),
                (int) dpToPixel(12),
                (int) dpToPixel(18));
        */
    }

    private void addDate() {
        date = getCurrentTime();
        TvDate.setText(date);
    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance(Locale.GERMANY);
        final int MINUTE = c.get(Calendar.MINUTE);
        final int HOUR = c.get(Calendar.HOUR_OF_DAY);
        final int DAY = c.get(Calendar.DAY_OF_MONTH);
        final int MONTH = c.get(Calendar.MONTH) + 1;
        final int YEAR = c.get(Calendar.YEAR);
        String date = "";
        if (DAY < 10) {
            date = "0" + DAY + "." +
                    (MONTH < 10 ? "0" + MONTH : MONTH) +
                    "." + YEAR;
        } else {
            date = DAY + "." +
                    (MONTH < 10 ? "0" + MONTH : MONTH) +
                    "." + YEAR;
        }

        String time = "";
        if (MINUTE < 10) {
            time = "" + HOUR + ":0" + MINUTE;
        } else {
            time = "" + HOUR + ":" + MINUTE;
        }
        return date + ", " + time;
    }
}
