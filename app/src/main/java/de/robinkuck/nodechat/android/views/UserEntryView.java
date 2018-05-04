package de.robinkuck.nodechat.android.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.robinkuck.nodechat.android.R;

public class UserEntryView extends RelativeLayout {

    private TextView tv_name;
    private String receipient;

    public UserEntryView(Activity activity, String receipient) {
        super(activity);
        configViews(activity, receipient);
    }

    private void configViews(Activity activity, final String recipient) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.userentry_view, this, true);

        RelativeLayout rl = (RelativeLayout) getChildAt(0);

        tv_name = (TextView) rl.getChildAt(0);

        this.receipient = recipient;
        tv_name.setText(this.receipient);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Adding private chats!
                /*
                CustomActivityManager.getInstance().startPrivateChatActivity(MainActivity.getInstance(), receipient);
                if(ChatlistFragment.getInstance().isUserAddedToChatList(receipient)) {

                } else {
                    ChatlistFragment.getInstance().addViewtoPrivateChatList(receipient);
                }
                */
            }
        });
    }

}
