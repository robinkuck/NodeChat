package de.robinkuck.nodechat.android;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by D070264 on 06.02.2018.
 */

public class NoInternetConnectionSnackbar {

    private Snackbar snackbar;

    public NoInternetConnectionSnackbar() {

    }

    public void show(final ViewGroup parent) {
        snackbar = Snackbar.make(parent, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.drawable.background_nointernetconnectionsnackbar);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.show();
    }

    public void dismiss() {
        if(snackbar!=null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
