package de.robinkuck.nodechat.android.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.ChangeNickDialog;
import de.robinkuck.nodechat.android.GlobalChatNotification;
import de.robinkuck.nodechat.android.activities.MainActivity;
import de.robinkuck.nodechat.android.history.GlobalHistoryMessage;
import de.robinkuck.nodechat.android.activities.ChatActivity;
import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.fragments.UserlistFragment;
import de.robinkuck.nodechat.android.utils.UiUtils;
import de.robinkuck.nodechat.android.utils.Utils;
import de.robinkuck.nodechat.android.SimpleNotification;
import de.robinkuck.nodechat.android.views.UserEntryView;
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
            socket = IO.socket("http://" + HOST + ":" +
                    (App.getInstance().isDebug() ? TEST_PORT : PORT) + "/", opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectSocket(final Context context) {
        initSocket();
        try {
            opts.query = "nick=" + NickManager.getInstance().getCurrentNick() + "&deviceid=" + Utils.getDeviceID(context)
                    + "&authkey=" + getAuthKey();
            socket.connect();
            System.out.println("[I] SocketManager: trying to connect...");
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
                e.printStackTrace();
            }
        }
    }

    public void sendPrivateMessage(String receiver, String message) {
        if (getSocket().connected()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("receiver", receiver);
                jsonObject.put("msg", message.trim());
                getSocket().emit("new_privatemessage", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeNick(final Activity activity, final String newNick, final ChangeNickDialog dialog) {
        if (getSocket().connected()) {
            final Ack ack = new Ack() {
                @Override
                public void call(Object... args) {
                    if ((boolean) args[0]) {
                        NickManager.getInstance().setCurrentNick(newNick);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(activity, "Nick successfully changed!", Toast.LENGTH_SHORT);
                                toast.show();
                                dialog.dismiss();
                            }
                        });
                        ;
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setErrorMsg("Nick already taken!");
                            }
                        });
                    }
                }
            };
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nick", newNick);
                getSocket().emit("change_nick", jsonObject, ack);
            } catch (JSONException e) {
                e.printStackTrace();
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
                    CustomActivityManager.getInstance().startMainActivity(App.getInstance().getBaseContext());
                }
                setStatus(Status.CONNECTED);
                System.out.println("[I] SocketManager: successful login!");
            }
        }).on("nologin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {
                    CustomActivityManager.getInstance().startNickActivity(CustomActivityManager.getInstance().getCurrentActivity());
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
        }).on("change_nickname_taken", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning()) {

                }
                disconnectSocket();
            }
        }).on("userConnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (Utils.isMyAppRunning() && CustomActivityManager.getInstance().getCurrentActivity() instanceof MainActivity) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String nick = data.getString("name");
                        UserlistFragment.getInstance().addViewtoUserList(nick,
                                new UserEntryView(UserlistFragment.getInstance().getActivity(), nick));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                        UserlistFragment.getInstance().removeViewFromUserList(nick);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                                            new UserEntryView(ChatlistFragment.getInstance().getActivity(), nick));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject dataObject = (JSONObject) args[0];
                try {
                    String from = dataObject.getString("from");
                    String message = dataObject.getString("msg");
                    String date = dataObject.getString("date");
                    boolean isReading;
                    if (CustomActivityManager.getInstance().getCurrentActivity() instanceof ChatActivity &&
                            ((ChatActivity) CustomActivityManager.getInstance().getCurrentActivity()).isActive()) {
                        isReading = true;
                    } else {
                        if (ChatHistoryManager.getInstance().getGlobalChatHistory().isMuted()) {

                        } else {
                            new GlobalChatNotification(App.getInstance().getApplicationContext(), from + ": " + message).show();
                        }
                        isReading = false;
                    }
                    ChatHistoryManager.getInstance().getGlobalChatHistory().addIncomingMessage(
                            new GlobalHistoryMessage(false, date, from, message), isReading);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
