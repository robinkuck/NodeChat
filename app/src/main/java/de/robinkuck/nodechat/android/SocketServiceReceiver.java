package de.robinkuck.nodechat.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kuckr on 20.01.2018.
 */

public class SocketServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        //startIntentService(context);
    }

    private void startIntentService(final Context context) {
        Intent i = new Intent(Intent.ACTION_SYNC, null, context, SocketServiceProvider.class);
        context.startService(i);
    }
}
