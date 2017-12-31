package kucki.com.socketdemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.R;

import static android.widget.LinearLayout.HORIZONTAL;

public class ChatActivity extends AppCompatActivity {

    public EditText editMsg;
    public Button sendButton;
    public ScrollView scroller;
    public LinearLayout msgs;

    public int x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInstance().setCurrentActivity(this);

        setContentView(R.layout.activity_chat);

        configViews();
        msgs = (LinearLayout) findViewById(R.id.rel);

        Display d = getWindowManager().getDefaultDisplay();
        x = d.getWidth();
        y = d.getHeight();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        App.getInstance().closeKeyboard(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        App.getInstance().closeKeyboard(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }).start();
        return true;
    }

    public void configViews() {
        editMsg = (EditText) findViewById(R.id.editMessage);
        scroller = (ScrollView) findViewById(R.id.scroller);
        sendButton = (Button) findViewById(R.id.sendButton);
    }

    protected void createPersonalMessageView(String msg) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.RIGHT);

        MessageView mv = new MessageView(this, msg, "", false, true, ((x / 3) * 2));
        l.addView(mv);
        msgs.addView(l);
    }

    //TODO: Add Writing Display
    //TODO: Add reading checkmark

    public void scrollDown() {
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
                //scroller.scrollTo(0,scroller.getBottom());
            }
        });
    }

    public void clearEditMsg() {
        editMsg.setText("");
    }

    //DP to Pixel
    public float dpToPixel(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
        /*
        final float density = getResources().getDisplayMetrics().density;
        return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
        */
    }
}
