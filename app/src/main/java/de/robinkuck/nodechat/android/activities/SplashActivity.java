package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.managers.SocketManager;
import de.robinkuck.nodechat.android.utils.Utils;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;

public class SplashActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CustomActivityManager.getInstance().setCurrentActivity(this);
        Utils.waitUntil(App.getInstance().isFirstRun() ? 2000 : 200, new Runnable() {
            @Override
            public void run() {
                switchActivity();
            }
        });
    }

    private void switchActivity() {
        if (SocketManager.getInstance().getStatus() == SocketManager.Status.CONNECTED) {
            CustomActivityManager.getInstance().startMainActivity(this);
        } else {
            CustomActivityManager.getInstance().startNickActivity(SplashActivity.this);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
