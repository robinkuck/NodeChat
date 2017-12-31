package kucki.com.socketdemo.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.socket.emitter.Emitter;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.HistoryManager;
import kucki.com.socketdemo.SocketManager;
import kucki.com.socketdemo.activities.MainActivity;
import kucki.com.socketdemo.R;

/**
 * Created by kuckr on 12.08.2017.
 */

public class NickFragment extends Fragment {

    private Button enter;
    private EditText editNick;
    private TextView notificationText;

    private static NickFragment INSTANCE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        INSTANCE = this;
        SocketManager.getInstance().disconnectSocket();
        return inflater.inflate(R.layout.fragment_nick, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        if(v!=null) {
            configViews();
        }
    }

    public static NickFragment getInstance() {
        return INSTANCE;
    }

    public TextView getNotificationText() {
        return notificationText;
    }

    public void setNotification(boolean visible) {
        if(visible) {
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

    public String getNickFromEntry() {
        return editNick.getText().toString();
    }

    private void configViews() {
        notificationText = (TextView)getView().findViewById(R.id.notification);
        editNick = (EditText)getView().findViewById(R.id.editNick);
        enter = (Button)getView().findViewById(R.id.buttonEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.getInstance().isOnline()) {
                    SocketManager.getInstance().connectSocket(editNick.getText().toString());
                } else {
                    App.getInstance().showNoInternetConnectionToast(getContext());
                }
            }
        });
    }
}
