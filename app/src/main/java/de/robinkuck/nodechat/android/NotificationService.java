package de.robinkuck.nodechat.android;

import android.app.IntentService;
import android.content.Intent;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        System.out.println("[I] starting NotificationService");
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHandleIntent(final Intent intent) {
    }

    @Override
    public void onTaskRemoved(final Intent intent) {
        System.out.println("[I] NotificationService task removed");
        new RelaunchNotification(getApplicationContext(), "Click to reopen", "You will not receive any messages now!").show();
    }

}
