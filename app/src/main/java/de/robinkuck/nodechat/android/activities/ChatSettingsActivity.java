package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import de.robinkuck.nodechat.android.R;

public class ChatSettingsActivity extends AbstractChildActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatsettings);
        super.configViews();
    }

    public void onClearHistory(final View view) {

    }
}
