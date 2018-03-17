package de.robinkuck.nodechat.android.managers;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import de.robinkuck.nodechat.android.CheckInternetConnectionReceiver;
import de.robinkuck.nodechat.android.SocketServiceProvider;
import de.robinkuck.nodechat.android.activities.GlobalChatActivity;
import de.robinkuck.nodechat.android.activities.MainActivity;
import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.activities.PrivateChatActivity;
import de.robinkuck.nodechat.android.activities.SettingsActivity;

/**
 * Created by kuckr on 23.08.2017.
 */

public class CustomActivityManager {

    private static CustomActivityManager INSTANCE;

    private Activity currentActivity = null;

    private CustomActivityManager() {
    }

    public static CustomActivityManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomActivityManager();
        }
        return INSTANCE;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        if (this.currentActivity == null) {
            addInternetConnectionReceiver(currentActivity);
            /*
            if(SocketManager.getInstance().getSocket()==null) {
                addSocketService(currentActivity);
            }*/

            if (SocketManager.getInstance().getSocket() == null) {
                addSocketService(currentActivity);
                System.out.println("[I] Starting SocktIntentService from ActivityManager");
            }
        }
        this.currentActivity = currentActivity;
    }

    public void startGlobalChatAcitity(final Activity current, final String nick) {
        final Intent intent = new Intent(current, GlobalChatActivity.class);
        intent.putExtra("nick", nick);
        new Thread(new Runnable() {
            @Override
            public void run() {
                current.startActivityIfNeeded(intent, 0);
            }
        }).start();
    }

    public void startPrivateChatActivity(final Activity currentActivity, final String receipient) {
        Intent intent = new Intent(currentActivity, PrivateChatActivity.class);
        intent.putExtra("chatwith", receipient);
        currentActivity.startActivityIfNeeded(intent, 0);
    }

    public void startMainActivity(final Activity currentActivity) {
        final Intent intent = new Intent(currentActivity, MainActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentActivity.startActivityIfNeeded(intent, 0);
            }
        }).start();
    }

    public void startNickActivity(final Activity currentActivity) {
        final Intent intent = new Intent(currentActivity, NickActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentActivity.startActivityIfNeeded(intent, 0);
            }
        }).start();
    }

    public void startSettingsActivity(final Activity currentActivity) {
        final Intent intent = new Intent(currentActivity, SettingsActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentActivity.startActivityIfNeeded(intent, 0);
            }
        }).start();
    }

    private void addInternetConnectionReceiver(final Activity startActivity) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        startActivity.getApplication().registerReceiver(new CheckInternetConnectionReceiver(), filter);
    }

    private void addSocketService(final Activity startActivity) {
        Intent i = new Intent(Intent.ACTION_SYNC, null, startActivity.getBaseContext(), SocketServiceProvider.class);
        i.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        startActivity.getBaseContext().startService(i);
    }

}
