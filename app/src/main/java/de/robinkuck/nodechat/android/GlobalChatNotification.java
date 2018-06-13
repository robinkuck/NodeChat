package de.robinkuck.nodechat.android;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import de.robinkuck.nodechat.android.activities.GlobalChatActivity;

public class GlobalChatNotification extends SimpleNotification{

    public GlobalChatNotification(final Context context, final String text) {
        super(context, "New global message", text);
        addRelaunchIntent();
    }

    private void addRelaunchIntent() {
        Intent intent = new Intent(super.context, GlobalChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(super.context, 0, intent, 0);
        super.builder.setContentIntent(pendingIntent);
        super.builder.setPriority(NotificationCompat.PRIORITY_MAX);
    }



}
