package kucki.com.socketdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import io.socket.emitter.Emitter;

/**
 * Created by kuckr on 12.08.2017.
 */

public class NickActivity extends AppCompatActivity {

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    private Button enter;
    private EditText editNick;
    private TextView notificationText;

    private String currentNick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nick_activity);
        App.current = this;

        configViews();

        if(isOnline()) {
            App.connectSocket();
            configSocketEvents();
        } else {
            App.showNoInternetConnectionToast();
        }


    }

    private void configSocketEvents() {
        final Context ct = this;
        final AppCompatActivity app = this;
        App.getSocket().on("suclogin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Intent intent = new Intent(ct,Chatlist.class);
                intent.putExtra("nick", currentNick);
                ActivityManager.startChatlistActivity(app, intent);
            }
        }).on("nologin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(notificationText.getVisibility() == View.INVISIBLE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setNotification(true);
                        }
                    });
                }
            }
        });
    }

    private void configViews() {
        notificationText = (TextView)findViewById(R.id.notification);
        editNick = (EditText)findViewById(R.id.editNick);
        enter = (Button)findViewById(R.id.buttonEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    checkNick(editNick.getText().toString());
                } else {
                    App.showNoInternetConnectionToast();
                }
            }
        });
    }

    private void checkNick(String nick) {
        if(App.getSocket().connected()&&!editNick.getText().toString().equals("")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("nick", nick);
                App.getSocket().emit("checkNick", obj);
                currentNick = nick;
            } catch(JSONException e) {
                System.out.println("Error sending data");
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void setNotification(boolean b) {
        if(b) {
            notificationText.setVisibility(View.VISIBLE);
            notificationText.setAlpha(0.0f);

            notificationText.animate().alpha(1f).setListener(null);
        } else {
            notificationText.setAlpha(1.0f);

            notificationText.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    notificationText.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

}
