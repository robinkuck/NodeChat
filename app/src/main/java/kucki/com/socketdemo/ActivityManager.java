package kucki.com.socketdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import kucki.com.socketdemo.activities.ChatActivity;
import kucki.com.socketdemo.activities.GlobalChatActivity;
import kucki.com.socketdemo.activities.MainActivity;

/**
 * Created by kuckr on 23.08.2017.
 */

public class ActivityManager {

    //Resuming Activities
    /*
    public static void startNickActivity(AppCompatActivity current) {
        Intent intent = new Intent(current,NickFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        current.startActivityIfNeeded(intent,0);
    }
    */

    public static void startGlobalChatAcitity(Activity curr) {
        final Activity current = curr;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(current, GlobalChatActivity.class);
                current.startActivityIfNeeded(intent,0);
            }
        }).start();
    }

    public static void startPrivateChatActivity(AppCompatActivity current, String name) {
        Intent intent = new Intent(current, ChatActivity.class);
        intent.putExtra("chatwith",name);
        current.startActivityIfNeeded(intent,0);
    }

}
