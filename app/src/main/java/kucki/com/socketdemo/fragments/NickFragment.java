package kucki.com.socketdemo.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.activities.MainActivity;
import kucki.com.socketdemo.R;

/**
 * Created by kuckr on 12.08.2017.
 */

public class NickFragment extends Fragment {

    private Button enter;
    private EditText editNick;
    private TextView notificationText;

    private String currentNick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nick, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        if(v!=null) {
            configViews();
        }

        if(isOnline()) {
            App.connectSocket();
            configSocketEvents();
        } else {
            App.showNoInternetConnectionToast(getContext());
        }

    }

    private void configSocketEvents() {
        App.getSocket().on("suclogin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ((MainActivity)getActivity()).setNick(currentNick);
                ((MainActivity)getActivity()).setChatlistFragment();
                App.closeKeyboard(getActivity());
            }
        }).on("nologin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(notificationText.getVisibility() == View.INVISIBLE) {
                    getActivity().runOnUiThread(new Runnable() {
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
        notificationText = (TextView)getView().findViewById(R.id.notification);
        editNick = (EditText)getView().findViewById(R.id.editNick);
        enter = (Button)getView().findViewById(R.id.buttonEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    checkNick(editNick.getText().toString());
                } else {
                    App.showNoInternetConnectionToast(getContext());
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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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
