package de.robinkuck.nodechat.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) inputView.findViewById(R.id.nickInput);
                final String input = editText.getText().toString().trim();
                if (!input.equals("") && !input.equals(NickManager.getInstance().getCurrentNick())) {
                    SocketManager.getInstance().changeNick(getActivity(), input);
                } else {
                    System.out.println("[I] ---");
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

}
