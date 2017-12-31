package kucki.com.socketdemo.activities;

import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kucki.com.socketdemo.App;
import kucki.com.socketdemo.CheckInternetConnectionReceiver;
import kucki.com.socketdemo.R;
import kucki.com.socketdemo.fragments.ChatlistFragment;
import kucki.com.socketdemo.fragments.NickFragment;

public class MainActivity extends AppCompatActivity {

    private static MainActivity INSTANCE;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        INSTANCE = this;

        App.getInstance().setCurrentActivity(this);

        setContentView(R.layout.activity_main);

        setNickFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }

    public void setNickFragment() {
        NickFragment fragment = new NickFragment();
        fragment.setArguments(getIntent().getExtras());

        FragmentTransaction transac = getSupportFragmentManager().beginTransaction();
        transac.replace(R.id.container,fragment);
        transac.addToBackStack(null);
        transac.commit();
    }

    public void setChatlistFragment() {
        ChatlistFragment fragment = new ChatlistFragment();
        fragment.setArguments(getIntent().getExtras());

        FragmentTransaction transac = getSupportFragmentManager().beginTransaction();
        transac.replace(R.id.container,fragment);
        transac.addToBackStack(null);
        transac.commit();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public static MainActivity getInstance() {
        return INSTANCE;
    }

}
