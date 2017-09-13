package kucki.com.socketdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kuckr on 20.08.2017.
 */

public class App extends Application {

    public static Context current;
    public static Socket socket;
    private static boolean debugMode = true;

    private static boolean online;

    public static void connectSocket() {
            if(socket!=null) {
                socket.disconnect();
            }
            try {
                socket = IO.socket("http://37.10.112.240:8011/");
                socket.connect();
            } catch (Exception e) {
                System.out.println(e);
            }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static boolean isOnline() {
        return online;
    }

    public static void setOnline(boolean pOnline) {
        online = pOnline;
    }

    public static void showNoInternetConnectionToast() {
        String txt = "Please check your Internet connection!";
        Toast t = Toast.makeText(current,txt,Toast.LENGTH_SHORT);
        t.show();
    }

}
