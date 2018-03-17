package de.robinkuck.nodechat.android.activities;

import android.view.MenuItem;

public abstract class AbstractChildActivity extends AbstractActivity {

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void configViews() {
        super.configViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void destroyActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                softKeyboard.closeSoftKeyboard();
                finish();
            }
        });
    }


}
