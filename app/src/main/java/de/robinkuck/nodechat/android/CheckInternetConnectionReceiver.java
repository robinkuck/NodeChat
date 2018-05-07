package de.robinkuck.nodechat.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;

import static android.content.ContentValues.TAG;

public class CheckInternetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "Network connectivity change");
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                if (!InternetConnectionManager.getInstance().isOnline()) {
                    if (SocketManager.getInstance().getSocket() != null) {
                        if (!SocketManager.getInstance().getSocket().connected()) {
                        }
                    }
                    InternetConnectionManager.getInstance().setOnline(true);
                    Log.d(TAG, "Network " + networkInfo.getTypeName() + " connected");
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                InternetConnectionManager.getInstance().setOnline(false);
                Log.d(TAG, "There's no network connectivity");
            }
        }
    }
}
