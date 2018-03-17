package de.robinkuck.nodechat.android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.robinkuck.nodechat.android.UiUtils;
import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.api.SoftKeyboard;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.NickManager;
import de.robinkuck.nodechat.android.managers.SocketManager;

public class NickActivity extends AppCompatActivity {

    private Button enter;
    private EditText editNick;
    private TextView notificationText;

    private RelativeLayout rootLayout;
    private SoftKeyboard softKeyboard;

    private static NickActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.activity_nick);
        configViews();
    }

    public static NickActivity getInstance() {
        return INSTANCE;
    }

    public TextView getNotificationText() {
        return notificationText;
    }

    private void setNotification(boolean visible) {
        if (visible) {
            notificationText.setVisibility(View.VISIBLE);
            notificationText.setAlpha(0.0f);

            notificationText.animate().alpha(1f).setListener(null);
        } else {
            notificationText.setAlpha(1.0f);

            notificationText.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    notificationText.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void setNotification(String message, boolean visible) {
        notificationText.setText(message);
        setNotification(visible);
    }

    public String getNickFromEntry() {
        return editNick.getText().toString();
    }

    private void configViews() {
        notificationText = (TextView) findViewById(R.id.notification);
        editNick = (EditText) findViewById(R.id.editNick);
        /*
        if(NickManager.getInstance().getCurrentNick().equals("")) {

        } else {
            editNick.setText(NickManager.getInstance().getCurrentNick());
        }
        */
        if (!NickManager.getInstance().getCurrentNick().equals("")) {
            editNick.setText(NickManager.getInstance().getCurrentNick());
        }
        enter = (Button) findViewById(R.id.buttonEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNick.getText().toString().trim().equals("")) {
                    setNotification("Invalid nick name!", true);
                } else {
                    /*if(NickManager.getInstance().getCurrentNick().equals(editNick.getText().toString().trim())) {
                        CustomActivityManager.getInstance().startMainActivity(NickActivity.this);
                        return;
                    } else {
                        NickManager.getInstance().setCurrentNick(editNick.getText().toString().trim());
                    }*/
                    if (InternetConnectionManager.getInstance().isOnline()) {
                        NickManager.getInstance().setCurrentNick(editNick.getText().toString().trim());
                        SocketManager.getInstance().connectSocket(getApplicationContext());
                    } else {
                        UiUtils.showNoInternetConnectionToast(getApplicationContext());
                    }
                }
            }
        });
        rootLayout = (RelativeLayout)

                findViewById(R.id.rootLayout);
        if (!InternetConnectionManager.getInstance().

                isOnline())

        {
            InternetConnectionManager.getInstance().showSnackbar(rootLayout);
        }
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
                                UiUtils.scrollDown((ScrollView) findViewById(R.id.scroller));
                            }
                        }, 200);
                    }
                });
            }
        });
    }
}
