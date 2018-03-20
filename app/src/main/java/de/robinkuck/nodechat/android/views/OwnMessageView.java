package de.robinkuck.nodechat.android.views;

import android.content.Context;

import de.robinkuck.nodechat.android.R;

public class OwnMessageView extends MessageView {

    public OwnMessageView(final Context ct, final String message, final int size) {
        super(ct, message, size);
        createMessageText();
    }

    public OwnMessageView(final Context ct, final String message, final String date, final int size) {
        super(ct, message, date, size);
        createMessageText();
    }

    @Override
    protected void createMessageText() {
        super.createMessageText();
        getTvMessage().setBackgroundResource(R.drawable.bgpersonalmessageview);
        getTvMessage().setPadding(getTvMessage().getPaddingLeft(), (int) dpToPixel(6),
                getTvMessage().getPaddingRight(), getTvMessage().getPaddingBottom());
    }

}
