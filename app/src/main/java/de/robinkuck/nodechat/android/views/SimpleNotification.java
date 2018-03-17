package de.robinkuck.nodechat.android.views;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import de.robinkuck.nodechat.android.R;

/**
 * Created by kuckr on 20.01.2018.
 */

public class SimpleNotification {

    protected NotificationCompat.Builder builder;
    protected Context context;
    protected Notification notification;

    public SimpleNotification(final Context context, final String title, final String text) {
        this.context = context;
        createNotification(context, title, text);
    }

    private void createNotification(final Context context, final String title, final String text) {
        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.launcher))
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
    }

    public void addIntent(final Intent intent) {
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
    }

    public void show() {
        notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, notification);
    }

}
