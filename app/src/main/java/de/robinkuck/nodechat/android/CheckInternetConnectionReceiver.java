package de.robinkuck.nodechat.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
import de.robinkuck.nodechat.android.utils.Utils;

import static android.content.ContentValues.TAG;

public class CheckInternetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "Network connectivity change");

        System.out.println("NETWORK CHANGE!!!");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                if (!InternetConnectionManager.getInstance().isOnline()) {
                    if (SocketManager.getInstance().getSocket() != null) {
                        if (!SocketManager.getInstance().getSocket().connected()) {
                            //SocketManager.getInstance().reconnectSocket();
                            System.out.println("[I] Reconnect Socket via ConnectionReceiver");
                        }
                    }
                    InternetConnectionManager.getInstance().setOnline(true);
                    Log.i(TAG, "Network " + networkInfo.getTypeName() + " connected");
                    Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
                    if (Utils.isAppRunning(context, "kucki.com.socketdemo")) {
                        changeOfflineView(false);
                    }
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                InternetConnectionManager.getInstance().setOnline(false);
                Log.d(TAG, "There's no network connectivity");
                Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
                if (Utils.isAppRunning(context, "de.robinkuck.nodechat")) {
                    changeOfflineView(true);
                }
            }
        }
    }

    private void changeOfflineView(final boolean visible) {
        System.out.println("[I] change Offline VIEW!");

        while (CustomActivityManager.getInstance().getCurrentActivity() == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        /*

        CustomActivityManager.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    offlineView.animate().alpha(1.0f).setDuration(500);
                } else {
                    offlineView.animate().alpha(0.0f).setDuration(500);
                }
            }
        });
        */
    }
}
