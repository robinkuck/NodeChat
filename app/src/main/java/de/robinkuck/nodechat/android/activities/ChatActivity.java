package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import de.robinkuck.nodechat.android.GlobalUtils;
import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.api.SoftKeyboard;
import de.robinkuck.nodechat.android.views.OwnMessageView;

import static android.widget.LinearLayout.HORIZONTAL;

public abstract class ChatActivity extends AbstractChildActivity {

    public EditText editMsg;
    public ImageButton sendButton;
    public ScrollView scroller;
    public LinearLayout msgs;
    private RelativeLayout rootLayout;

    protected int x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        configViews();
        configKeyboard();
        msgs = (LinearLayout) findViewById(R.id.rel);

        Display d = getWindowManager().getDefaultDisplay();
        x = d.getWidth();
        y = d.getHeight();
    }

    public void configViews() {
        super.configViews();
        editMsg = (EditText) findViewById(R.id.editMessage);
        editMsg.setFocusableInTouchMode(true);
        //editMsg.setOnFocusChangeListener(new MyFocusChangeCloseKeyboardListener(super.softKeyboard));
        scroller = (ScrollView) findViewById(R.id.scroller);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
    }

    public void configKeyboard() {
        super.configKeyboard(rootLayout);
        super.softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {

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
                                GlobalUtils.scrollDown(scroller);
                            }
                        }, 200);
                    }
                });
            }
        });
        /*
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
                                GlobalUtils.scrollDown(scroller);
                            }
                        }, 200);
                    }
                });
            }
        });
        */
    }

    protected OwnMessageView createPersonalMessageView(String message) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.RIGHT);

        OwnMessageView mv = new OwnMessageView(this, message.trim(), ((x / 3) * 2));
        l.addView(mv);
        msgs.addView(l);
        return mv;
    }

    //TODO: Add Writing Display
    //TODO: Add reading checkmark

    public void clearEditMsg() {
        editMsg.setText("");
    }
}
