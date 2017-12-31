package kucki.com.socketdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {

    private static App INSTANCE;

    public String currentNick;

    private boolean debugMode = true;

    private final String ADDRESS = "robinkuck.de";
    private final String PORT = "8011";

    private boolean online;

    private Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        addReceiver();
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean pOnline) {
        online = pOnline;
    }

    public void showNoInternetConnectionToast(Context ct) {
        String txt = "Please check your Internet connection!";
        Toast t = Toast.makeText(ct,txt,Toast.LENGTH_SHORT);
        t.show();
    }

    public void checkNick(String nick) {
        if (SocketManager.getInstance().getSocket().connected()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("nick", nick);
                SocketManager.getInstance().getSocket().emit("checkNick", obj);
                currentNick = nick;
            } catch (JSONException e) {
                System.out.println("Error sending data");
            }
        }
    }

    //TODO: Implement callback of nick check!

    public void closeKeyboard(Activity act) {
        final Activity activity = act;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {

                }
            }
        }).start();
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    private void addReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new CheckInternetConnectionReceiver(),filter);
    }

}
