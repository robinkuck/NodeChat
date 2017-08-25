package kucki.com.socketdemo;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNickFragment();
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

}
