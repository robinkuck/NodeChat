package de.robinkuck.nodechat.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationDialog extends AlertDialog.Builder {

    public ConfirmationDialog(final Context context, final String title, final String message,
                              final DialogInterface.OnClickListener positiveButtonOnClickListener,
                              final DialogInterface.OnClickListener negativeButtonOnClickListener) {
        super(context);
        setTitle(title);
        setMessage(message);
        setPositiveButton(R.string.yes, positiveButtonOnClickListener);
        setNegativeButton(R.string.cancel, negativeButtonOnClickListener);
    }
}
