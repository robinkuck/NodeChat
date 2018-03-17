package de.robinkuck.nodechat.android;

import android.app.Application;

import de.robinkuck.nodechat.android.managers.ChatHistoryManager;

public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        //System.out.println("[I] Starting SocketServiceProvider from Application");
        //startService(new Intent(this,SocketServiceProvider.class));
        ChatHistoryManager.getInstance();
    }

    public static App getInstance() {
        return INSTANCE;
    }


}
