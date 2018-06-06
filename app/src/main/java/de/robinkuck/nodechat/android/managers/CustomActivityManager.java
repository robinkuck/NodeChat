package de.robinkuck.nodechat.android.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import de.robinkuck.nodechat.android.CheckInternetConnectionReceiver;
import de.robinkuck.nodechat.android.SocketServiceProvider;
import de.robinkuck.nodechat.android.activities.GlobalChatActivity;
import de.robinkuck.nodechat.android.activities.MainActivity;
import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.activities.PrivateChatActivity;
import de.robinkuck.nodechat.android.activities.SettingsActivity;

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

    public void startGlobalChatAcitity(final Context context, final String nick) {
        final Intent intent = new Intent(context, GlobalChatActivity.class);
        intent.putExtra("nick", nick);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startPrivateChatActivity(final Context context, final String receipient) {
        Intent intent = new Intent(context, PrivateChatActivity.class);
        intent.putExtra("chatwith", receipient);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startMainActivity(final Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startNickActivity(final Context context) {
        final Intent intent = new Intent(context, NickActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startSettingsActivity(final Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
