package de.robinkuck.nodechat.android;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import de.robinkuck.nodechat.android.managers.ChatHistoryManager;

public class App extends Application {

    private static App INSTANCE;
    private boolean firstRun = true;
    private NotificationChannel notificationChannel;
    private String channelID = "42021";

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelID,
                    App.getInstance().getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
        }
        startService(new Intent(this, NotificationService.class));
        ChatHistoryManager.getInstance().loadData();
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }

    public boolean isFirstRun() {
        if (firstRun) {
            firstRun = false;
            return true;
        } else {
            return false;
        }
    }
}
