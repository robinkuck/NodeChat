package de.robinkuck.nodechat.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import de.robinkuck.nodechat.android.utils.Utils;

public class SocketServiceProvider extends IntentService {

    public SocketServiceProvider() {
        super("SocketServiceProvider");
    }

    @Override
    public void onCreate() {
        System.out.println("[I] SocketServiceProvider created!");

        startSocketConnection();

        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        System.out.println("[I] SocketServiceProvider started! " + startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("[I] SocketServiceProvider destroyed!");
        super.onDestroy();
    }

    @Override
    public void onHandleIntent(final Intent intent) {
        System.out.println("[I] SocketServiceProvider handle intent!");
    }

    @Override
    public void onTaskRemoved(final Intent intent) {
        System.out.println("[I] SocketServiceProvider task removed!");
        Utils.waitUntil(1500, new Runnable() {
            @Override
            public void run() {
                new RelaunchNotification(getApplicationContext(), "Click to reopen", "You will not receive any messages now!").show();
            }
        });
    }

    private void startSocketConnection() {
        String savedNick = getApplicationContext().getSharedPreferences("chatapp", Context.MODE_PRIVATE).getString("nickName", "");
        System.out.println("[I] SocketServiceProvider: Saved Nick: " + savedNick);
    }

}
