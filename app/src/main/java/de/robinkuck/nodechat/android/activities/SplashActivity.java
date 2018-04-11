package de.robinkuck.nodechat.android.activities;

import android.content.Intent;
import android.os.Bundle;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.SocketServiceProvider;
import de.robinkuck.nodechat.android.utils.Utils;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;

public class SplashActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CustomActivityManager.getInstance().setCurrentActivity(this);
        //startService(new Intent(this, SocketServiceProvider.class));
        Utils.waitUntil(2500, new Runnable() {
            @Override
            public void run() {
                switchActivity();
                finish();
            }
        });
    }

    private void switchActivity() {
        CustomActivityManager.getInstance().startNickActivity(SplashActivity.this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
