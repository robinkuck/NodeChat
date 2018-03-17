package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import de.robinkuck.nodechat.android.R;

public class SettingsActivity extends AbstractChildActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        configViews();
    }

    public void onNickChange(View v) {
        System.out.println("[I] Try to change nick!");
    }

    @Override
    public void configViews() {
        super.configViews();
    }

}
