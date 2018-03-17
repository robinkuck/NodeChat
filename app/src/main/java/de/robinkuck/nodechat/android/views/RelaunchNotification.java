package de.robinkuck.nodechat.android.views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import de.robinkuck.nodechat.android.activities.SplashActivity;

/**
 * Created by D070264 on 27.02.2018.
 */

public class RelaunchNotification extends SimpleNotification {

    public RelaunchNotification(final Context context, final String title, final String text) {
        super(context, title, text);
        addRelaunchIntent();
    }

    private void addRelaunchIntent() {
        Intent intent = new Intent(super.context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(super.context, 0, intent, 0);
        super.builder.setContentIntent(pendingIntent);
        super.builder.setPriority(NotificationCompat.PRIORITY_MAX);
    }

}
