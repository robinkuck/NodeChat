package de.robinkuck.nodechat.android.activities;

import android.app.Service;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.api.SoftKeyboard;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;

public abstract class AbstractActivity extends AppCompatActivity {

    public SoftKeyboard softKeyboard;

    public void configViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(4.0f);
    }

    public void configKeyboard(final ViewGroup rootLayout) {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(rootLayout, im);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomActivityManager.getInstance().setCurrentActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (softKeyboard != null) {
            softKeyboard.closeSoftKeyboard();
        }
    }
}
