package kucki.com.socketdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import kucki.com.socketdemo.activities.ChatActivity;
import kucki.com.socketdemo.activities.GlobalChatActivity;
import kucki.com.socketdemo.activities.MainActivity;

/**
 * Created by kuckr on 23.08.2017.
 */

public class ActivitySwitcher {

    public static void startGlobalChatAcitity(final Activity current, final String nick) {
            final Intent intent = new Intent(current, GlobalChatActivity.class);
            intent.putExtra("nick", nick);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    current.startActivityIfNeeded(intent, 0);
                }
            }).start();
    }

    public static void startPrivateChatActivity(AppCompatActivity current, String name) {
        Intent intent = new Intent(current, ChatActivity.class);
        intent.putExtra("chatwith",name);
        current.startActivityIfNeeded(intent,0);
    }

}
