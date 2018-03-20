package de.robinkuck.nodechat.android.views;

import android.content.Context;

import de.robinkuck.nodechat.android.R;

public abstract class ForeignMessageView extends MessageView {

    private String name;

    public ForeignMessageView(final Context ct, final String message, final String name, final int size) {
        super(ct, message, size);
        this.name = name;
    }

    public ForeignMessageView(final Context ct, final String message, final String name, final String date, final int size) {
        super(ct, message, date, size);
        this.name = name;
    }

    protected void addName() {
        getTvName().setText(getName());
    }

    @Override
    protected void createMessageText() {
        super.createMessageText();
        getTvName().setText(name);
        getTvMessage().setBackgroundResource(R.drawable.background_messageview);
        getTvMessage().setPadding(getTvMessage().getPaddingLeft(), (int) dpToPixel(17),
                getTvMessage().getPaddingRight(), getTvMessage().getPaddingBottom());
    }

    public String getName() {
        return name;
    }

}
