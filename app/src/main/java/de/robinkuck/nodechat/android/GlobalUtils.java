package de.robinkuck.nodechat.android;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by Robin on 11.01.18.
 */

public class GlobalUtils {

    public static void showNoInternetConnectionToast(final Context ct) {
        String txt = "Please check your Internet connection!";
        Toast t = Toast.makeText(ct,txt,Toast.LENGTH_SHORT);
        t.show();
    }

    public static void closeKeyboard(final Activity act) {
        final Activity activity = act;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {

                }
            }
        }).start();
    }

    public static void scrollDown(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
