package de.robinkuck.nodechat.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.robinkuck.nodechat.android.managers.NickManager;
import de.robinkuck.nodechat.android.managers.SocketManager;

public class ChangeNickDialog extends DialogFragment {

    private View inputView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inputView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_nick, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inputView);
        builder.setTitle("Enter a new nick:");
        builder.setPositiveButton("Save", null /*new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) inputView.findViewById(R.id.nickInput);
                final String input = editText.getText().toString().trim();
                if (!input.equals("") && !input.equals(NickManager.getInstance().getCurrentNick())) {
                    SocketManager.getInstance().changeNick(getActivity(), input);
                    dismiss();
                } else {
                    System.out.println("[I] ---");
                }
            }
        }*/);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editText = (EditText) inputView.findViewById(R.id.nickInput);
                        final String input = editText.getText().toString().trim();
                        if (input.equals("")) {
                            setErrorMsg("Please enter something!");
                            return;
                        }
                        if(input.equals(NickManager.getInstance().getCurrentNick())) {
                            setErrorMsg("This is already your nick!");
                            return;
                        }
                        SocketManager.getInstance().changeNick(getActivity(), input, ChangeNickDialog.this);
                    }
                });
            }
        });
        return dialog;
    }

    public void setErrorMsg(final String errorMsg) {
        final TextView textView = (TextView) inputView.findViewById(R.id.notification);
        textView.setText(errorMsg);
        textView.setVisibility(View.VISIBLE);
        textView.setAlpha(0.0f);
        textView.animate().alpha(1f).setListener(null);
    }

}
