package de.robinkuck.nodechat.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class SimpleNotification {

    protected static String channelID = "42021";
    protected NotificationCompat.Builder builder;
    protected Context context;
    protected Notification notification;

    public SimpleNotification(final Context context, final String title, final String text) {
        this.context = context;
        createNotification(context, title, text);
    }

    private void createNotification(final Context context, final String title, final String text) {
        builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.mipmap.launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.launcher))
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
    }

    public void addIntent(final Intent intent) {
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
    }

    public void show() {
        notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(App.getInstance().getNotificationChannel());
        }
        notificationManager.notify(001, notification);
    }

}
