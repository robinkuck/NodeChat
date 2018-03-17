package de.robinkuck.nodechat.android.managers;

import android.app.Activity;
import android.view.ViewGroup;

import de.robinkuck.nodechat.android.NoInternetConnectionSnackbar;
import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.activities.MainActivity;
import de.robinkuck.nodechat.android.activities.NickActivity;

/**
 * Created by Robin on 11.01.18.
 */

public class InternetConnectionManager {

    private static InternetConnectionManager INSTANCE;

    private boolean isOnline = false;
    private NoInternetConnectionSnackbar snackbar;

    private InternetConnectionManager() {
        snackbar = new NoInternetConnectionSnackbar();
    }

    public static InternetConnectionManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InternetConnectionManager();
        }
        return INSTANCE;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(final boolean isOnline) {
        this.isOnline = isOnline;
        if(isOnline) {
            snackbar.dismiss();
        } else {
            final Activity currentActivity = CustomActivityManager.getInstance().getCurrentActivity();
            if(currentActivity!=null && (currentActivity instanceof MainActivity)||(currentActivity instanceof NickActivity)) {
                snackbar.show((ViewGroup)currentActivity.findViewById(R.id.rootLayout));
            }
        }
    }

    public void showSnackbar(final ViewGroup parent) {
        snackbar.show(parent);
    }



}
