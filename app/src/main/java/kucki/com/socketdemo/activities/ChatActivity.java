package kucki.com.socketdemo.activities;

import android.app.Service;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.ScrollView;

import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.R;
import kucki.com.socketdemo.api.SoftKeyboard;

import static android.widget.LinearLayout.HORIZONTAL;

public class ChatActivity extends AppCompatActivity {

    public EditText editMsg;
    public Button sendButton;
    public ScrollView scroller;
    public LinearLayout msgs;
    private RelativeLayout rootLayout;
    private SoftKeyboard softKeyboard;

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
        softKeyboard.closeSoftKeyboard();
        new Thread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        softKeyboard.closeSoftKeyboard();
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
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        configKeyboard();
    }

    private void configKeyboard() {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(rootLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                //do nothing
            }

            @Override
            public void onSoftKeyboardShow() {
                System.out.println("SHOWING KEYBOARD!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollDown();
                            }
                        }, 200);
                    }
                });
            }
        });
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
}
