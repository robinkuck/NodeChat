package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.InternetConnectionManager;
import de.robinkuck.nodechat.android.managers.SocketManager;
import de.robinkuck.nodechat.android.views.GlobalChatForeignMessageView;
import de.robinkuck.nodechat.android.views.MessageView;
import de.robinkuck.nodechat.android.views.OwnMessageView;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class GlobalChatActivity extends ChatActivity {

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configSocketEvents();
        configSendButton();
        getSupportActionBar().setTitle("Global chat");

        nick = getIntent().getStringExtra("nick");
        //loadHistory();
        ChatlistFragment.getInstance().getGlobalChatlistEntry().resetTVUnreadMessagesCount();
        if(ChatHistoryManager.getInstance().getGlobalChatHistory().getUnreadMessagesCount()>0) {
            ChatHistoryManager.getInstance().getGlobalChatHistory().resetUnreadMessagesCount();
        }

        super.adapter = new ListViewAdapter(ChatHistoryManager.getInstance().getGlobalChatHistory().getMessages());
        super.recyclerView.setAdapter(super.adapter);
        scrollToBottom();

        ChatlistFragment.getInstance().getGlobalChatlistEntry().resetTVUnreadMessagesCount();
        if(ChatHistoryManager.getInstance().getGlobalChatHistory().getUnreadMessagesCount()>0) {
            ChatHistoryManager.getInstance().getGlobalChatHistory().resetUnreadMessagesCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomActivityManager.getInstance().setCurrentActivity(this);
    }

    private void configSocketEvents() {
        SocketManager.getInstance().getSocket().on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String from = data.getString("from");
                    final String message = data.getString("msg");
                    final String date = data.getString("date");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createGlobalChatForeignMessageView(message, from);
                            //UiUtils.scrollDown(scroller);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void configSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMsg.getText().toString().trim().equals("")) {

                } else {
                    if (InternetConnectionManager.getInstance().isOnline()) {
                        final OwnMessageView messageView = createPersonalMessageView(editMsg.getText().toString());
                        SocketManager.getInstance().sendGlobalMessage(messageView.getMessage(), messageView.getDate(),
                                new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        System.out.println("[I] Global Message was sent successfully!");
                                        messageView.setSuccessfullSent(true);
                                    }
                                });
                        clearEditMsg();
                        //UiUtils.scrollDown(scroller);
                        ChatHistoryManager.getInstance().getGlobalChatHistory().addSentMessage(
                                new GlobalHistoryMessage(true,messageView.getDate(),"", messageView.getMessage())
                        );
                    }
                }
            }
        });
    }

    private void createGlobalChatForeignMessageView(final String msg, final String name) {
        /*
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);
        */

        GlobalChatForeignMessageView mv = new GlobalChatForeignMessageView(this, msg, name, super.getMessageViewWidth());
        /*
        l.addView(mv);
        msgs.addView(l);
        */
        //msgs.addView(mv);
        System.out.println("[I] GChatMessageView created!");
    }

    private void createGlobalChatHistoryMessageView(final String msg, final String name, final String date, final boolean isPersonal) {
        /*
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);
        */
        if (isPersonal) {
            super.createPersonalHistoryMessageView(msg, date);
        } else {
            MessageView mv = new GlobalChatForeignMessageView(this, msg, name, date, super.getMessageViewWidth());
            //msgs.addView(mv);
            //l.addView(mv);
        }
    }

    private void loadHistory() {
        for (final GlobalHistoryMessage message : ChatHistoryManager.getInstance().getGlobalChatHistory().getMessages()) {
            createGlobalChatHistoryMessageView(message.getMessageString(), message.getNameString(), message.getDateString(), message.isPersonal());
        }
        //UiUtils.scrollDown(scroller);
    }

    /*
    private void loadHistory2() {
        ArrayAdapter<MessageView> messageViewArrayAdapter = new ArrayAdapter<>(
                getCallingActivity(), R.layout.activity_chat, R.layout.message_view, )
    }
    */
}
