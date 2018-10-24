package de.robinkuck.nodechat.android.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

public class UiUtils {

    public static void showNoInternetConnectionToast(final Context ct) {
        String txt = "Please check your Internet connection!";
        Toast t = Toast.makeText(ct,txt,Toast.LENGTH_SHORT);
        t.show();
    }

    public static void closeKeyboard(final Activity act) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            act.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {

                }
            }
        }).start();
    }

    public static void showToast(final Activity activity, final String message, final int duration) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity, message, duration);
                toast.show();
            }
        });
    }

    public static void scrollDown(final ScrollView scrollView) {
        if(scrollView.isSmoothScrollingEnabled()) {
            scrollView.setSmoothScrollingEnabled(false);
        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
