package kucki.com.socketdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.Socket;

import static android.content.ContentValues.TAG;

public class CheckInternetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                if (!App.getInstance().isOnline()) {
                    SocketManager.getInstance().reconnectSocket();
                    App.getInstance().setOnline(true);
                    Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                    Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
                    changeOfflineView(false);
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                App.getInstance().setOnline(false);
                Log.d(TAG, "There's no network connectivity");
                Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
                changeOfflineView(true);
            }
        }
    }

    private void changeOfflineView(final boolean visible) {
        final View offlineView = App.getInstance().getCurrentActivity().findViewById(R.id.offine_info);

        System.out.println("[I] change Offline VIEW!");

        while(App.getInstance().getCurrentActivity()==null) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {

            }
        }

            App.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (visible) {
                        offlineView.animate().alpha(1.0f).setDuration(500);
                    } else {
                        offlineView.animate().alpha(0.0f).setDuration(500);
                    }
                }
            });
    }
}
