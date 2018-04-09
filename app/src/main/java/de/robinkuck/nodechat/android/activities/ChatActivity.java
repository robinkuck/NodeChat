package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.api.SoftKeyboard;
import de.robinkuck.nodechat.android.history.HistoryMessage;
import de.robinkuck.nodechat.android.utils.Utils;
import de.robinkuck.nodechat.android.views.OwnMessageView;

public abstract class ChatActivity<messageObj extends HistoryMessage> extends AbstractChildActivity {

    public EditText editMsg;
    public ImageButton sendButton;
    //public ScrollView scroller;
    //public LinearLayout msgs;
    private RelativeLayout rootLayout;
    private boolean isActive;

    protected int x, y;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected List<messageObj> messageDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        configViews();
        configKeyboard();
        //msgs = (LinearLayout) findViewById(R.id.rel);

        Display d = getWindowManager().getDefaultDisplay();
        x = d.getWidth();
        y = d.getHeight();

        isActive = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    public void configViews() {
        super.configViews();
        editMsg = (EditText) findViewById(R.id.editMessage);
        editMsg.setFocusableInTouchMode(true);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void configKeyboard() {
        super.configKeyboard(rootLayout);
        super.softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {

            }

            @Override
            public void onSoftKeyboardShow() {
                System.out.println("SHOWING KEYBOARD!");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.waitUntil(150, new Runnable() {
                            @Override
                            public void run() {
                                scrollToBottom();
                            }
                        });
                    }

                });
            }
        });
    }

    //TODO: Add Writing Display
    //TODO: Add reading checkmark

    public void clearEditMsg() {
        editMsg.setText("");
    }

    public boolean isActive() {
        return isActive;
    }

    protected int getMessageViewWidth() {
        return ((x / 3) * 2);
    }

    protected void scrollToBottom() {
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    protected void addMessage() {
        adapter.notifyItemInserted(messageDataSet.size() - 1);
        scrollToBottom();
    }

    protected String getCurrentDateString() {
        Calendar c = Calendar.getInstance(Locale.GERMANY);
        final int MINUTE = c.get(Calendar.MINUTE);
        final int HOUR = c.get(Calendar.HOUR_OF_DAY);
        final int DAY = c.get(Calendar.DAY_OF_MONTH);
        final int MONTH = c.get(Calendar.MONTH) + 1;
        final int YEAR = c.get(Calendar.YEAR);
        String date = "";
        if (DAY < 10) {
            date = "0" + DAY + "." +
                    (MONTH < 10 ? "0" + MONTH : MONTH) +
                    "." + YEAR;
        } else {
            date = DAY + "." +
                    (MONTH < 10 ? "0" + MONTH : MONTH) +
                    "." + YEAR;
        }

        String time = "";
        if (MINUTE < 10) {
            time = "" + HOUR + ":0" + MINUTE;
        } else {
            time = "" + HOUR + ":" + MINUTE;
        }
        return date + ", " + time;
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MessageViewHolder> {

        private final List<? extends HistoryMessage> dataSet;

        public ListViewAdapter(final List<? extends HistoryMessage> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataSet.get(position).isPersonal()) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MessageViewHolder holder = null;
            View view = null;
            switch (viewType) {
                case 0:
                    view = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.own_message, null);
                    holder = new OwnMessageViewHolder(view);
                    break;
                case 1:
                    view = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.global_message, null);
                    holder = new GlobalForeignMessageViewHolder(view);
                    break;
            }
            return holder;
        }


        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            holder.message.setText(dataSet.get(position).getMessageString());
            holder.date.setText(dataSet.get(position).getDateString());
            if (holder.name != null) {
                holder.name.setText(dataSet.get(position).getNameString());
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {

            public TextView message;
            public TextView name;
            public TextView date;

            public MessageViewHolder(final View view) {
                super(view);
                message = (TextView) view.findViewById(R.id.vmessage);
                date = (TextView) view.findViewById(R.id.vdate);
                message.setMaxWidth(getMessageViewWidth());
            }
        }

        public class OwnMessageViewHolder extends MessageViewHolder {

            public OwnMessageViewHolder(final View view) {
                super(view);
            }
        }

        public class GlobalForeignMessageViewHolder extends MessageViewHolder {

            public GlobalForeignMessageViewHolder(final View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.vname);
            }
        }
    }

}
