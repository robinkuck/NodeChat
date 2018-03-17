package de.robinkuck.nodechat.android.managers;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.GlobalMessage;
import de.robinkuck.nodechat.android.activities.GlobalChatActivity;
import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.fragments.UserlistFragment;
import de.robinkuck.nodechat.android.utils.Utils;
import de.robinkuck.nodechat.android.views.MessageView;
import de.robinkuck.nodechat.android.views.SimpleNotification;
import de.robinkuck.nodechat.android.views.UserEntry;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    static {
        System.loadLibrary("keys");
    }

    public native int getTestPort();
    public native int getPort();
    public native String getHost();
    public native String getAuthKey();

    private static SocketManager INSTANCE;

    public enum Status {
        DISCONNECTED, CONNECTED
    }

    private Status status = Status.DISCONNECTED;

    private Socket socket;
    private final String HOST = getHost();
    private final String LOCAL_HOST = "127.0.0.1";
    private final int PORT = getPort();
    private final int TEST_PORT = getTestPort();
    private IO.Options opts;

    private SocketManager() {
        opts = new IO.Options();
    }

    public void initSocket() {
        if (socket != null) {
            socket.disconnect();
        }
        try {
            socket = IO.socket("http://" + HOST + ":" + TEST_PORT + "/", opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectSocket(final Context context) {
        initSocket();
        try {
            opts.query = "nick=" + NickManager.getInstance().getCurrentNick() + "&deviceid=" + Utils.getDeviceID(context)
                + "&authkey=" + getAuthKey();
            //socket = IO.socket("http://" + HOST + ":" + PORT + "/", opts);
            System.out.println("[I] SocketManager: trying to connect...");
            socket.connect();
            System.out.println("[I] SocketManager: connection etablished!");
            configSocketEvents();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void disconnectSocket() {
        if (getSocket() != null && getSocket().connected()) {
            getSocket().disconnect();
            status = Status.DISCONNECTED;
            System.out.println("[I] SocketManager: connection lost!");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public static SocketManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SocketManager();
        }
        return INSTANCE;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void sendGlobalMessage(final String message, final String date, final Ack ack) {
        if (getSocket().connected()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("msg", message);
                jsonObject.put("date", date);
                getSocket().emit("new_globalmessage", jsonObject, ack);
            } catch (JSONException e) {
                System.out.println("Error sending global message");
            }
        }
    }

    public void sendPrivateMessage(String receiver, String message) {
        if (getSocket().connected()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("receiver", receiver);
                jsonObject.put("msg", message.trim());
                SocketManager.getInstance().getSocket().emit("new_privatemessage", jsonObject);
            } catch (JSONException e) {
                System.out.println("Error sending private message");
            }
        }
    }

    private void configSocketEvents() {
        //TODO: Implement all incoming socket Events
        getSocket().on("suclogin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    NickManager.getInstance().setCurrentNick(NickManager.getInstance().getCurrentNick());
                    CustomActivityManager.getInstance().startMainActivity(NickActivity.getInstance());
                }
                SocketManager.getInstance().setStatus(Status.CONNECTED);
                System.out.println("[I] SocketManager: successful login!");
            }
        }).on("nologin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    CustomActivityManager.getInstance().startNickActivity(CustomActivityManager.getInstance().getCurrentActivity());
                    //disconnectSocket();
                }
            }
        }).on("nickname_taken", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    if (NickActivity.getInstance().getNotificationText().getVisibility() == View.INVISIBLE) {
                        NickActivity.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NickActivity.getInstance().setNotification("Nickname already taken!", true);
                            }
                        });
                    }
                }
                disconnectSocket();
            }
        }).on("userConnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String nick = data.getString("name");
                        System.out.println(nick);
                    /*
                    ChatlistFragment.getInstance().addViewtoPrivateChatList(nick,
                            new ChatlistEntry(ChatlistFragment.getInstance().getActivity(), nick));*/
                        UserlistFragment.getInstance().addViewtoUserList(nick,
                                new UserEntry(UserlistFragment.getInstance().getActivity(), nick));
                    } catch (JSONException e) {
                        System.out.println("Error getting connected user!");
                    }
                }
            }
        }).on("userDisconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String nick = data.getString("name");
                        //ChatlistFragment.getInstance().removeViewFromPrivateChatList(nick);
                        UserlistFragment.getInstance().removeViewFromUserList(nick);
                    } catch (JSONException e) {
                        System.out.println("Error getting disconnected user!");
                    }
                }
            }
        }).on("allUsers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    if (UserlistFragment.getInstance() != null) {
                        JSONArray data = (JSONArray) args[0];
                        UserlistFragment.getInstance().clearUserList();
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject user = data.getJSONObject(i);
                                if (user.getString("name").equals(NickManager.getInstance().getCurrentNick())) {

                                } else {
                                    String nick = user.getString("name");
                                    System.out.println("User: " + nick);
                                    UserlistFragment.getInstance().addViewtoUserList(nick,
                                            new UserEntry(ChatlistFragment.getInstance().getActivity(), nick));

                                }
                            } catch (JSONException e) {
                                System.out.println("Error getting all users!");

                            }
                        }
                    }
                }
            }
        }).on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("global message!!!");
                JSONObject dataObject = (JSONObject)args[0];

                /*
                ChatHistoryManager.getInstance().getGlobalChatHistory().addMessage(
                        new GlobalMessage(false, dataObject.getString(), dataObject.getString("from"), dataObject.getString("msg"));
                );
                */

                if(CustomActivityManager.getInstance().getCurrentActivity() instanceof GlobalChatActivity) {

                } else {
                    new SimpleNotification(App.getInstance().getApplicationContext(),"New global message", "").show();
                }
            }
        });
    }
}
