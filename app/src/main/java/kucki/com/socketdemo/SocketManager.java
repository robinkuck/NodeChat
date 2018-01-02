package kucki.com.socketdemo;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kucki.com.socketdemo.Views.ChatlistEntry;
import kucki.com.socketdemo.activities.MainActivity;

import kucki.com.socketdemo.fragments.ChatlistFragment;
import kucki.com.socketdemo.fragments.NickFragment;

public class SocketManager {
    private static SocketManager INSTANCE = new SocketManager();

    private Socket socket;
    private final String ADDRESS = "robinkuck.de";
    private final String PORT = "8011";
    private IO.Options opts;

    private SocketManager() {
        opts = new IO.Options();
        System.out.println("HI!!!");
    }

    public void connectSocket(final String nickName) {
        if(socket!=null) {
            socket.disconnect();
        }
        try {
            opts.query = "nick=" + nickName;
            opts.reconnection = false;
            opts.timeout = -1;

            socket = IO.socket("http://" + ADDRESS + ":" + PORT + "/", opts);
            socket.connect();

            configSocketEvents();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void disconnectSocket() {
        if(getSocket()!=null && getSocket().connected()) {
            getSocket().disconnect();
        }
    }

    public void reconnectSocket() {
        if(getSocket()!=null) {
            try {
                socket = IO.socket("http://" + ADDRESS + ":" + PORT + "/", opts);
                socket.connect();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public boolean isConnectedWithNick() {
        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    public static SocketManager getInstance() {
        return INSTANCE;
    }

    public void sendGlobalMessage(String message) {
        if (getSocket().connected()) {
            JSONObject data = new JSONObject();
            try {
                data.put("msg", message.trim());
                SocketManager.getInstance().getSocket().emit("new_globalmessage", data);
                System.out.println("[I] Sending global message!");
            } catch (JSONException e) {
                System.out.println("Error sending data");
            }
        }
    }

    public void sendPrivateMessage(String from, String to, String message) {

    }

    private void configSocketEvents() {
        //TODO: Implement all incoming socket Events
        getSocket().on("suclogin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainActivity.getInstance().setChatlistFragment();
                App.getInstance().closeKeyboard(NickFragment.getInstance().getActivity());
                App.getInstance().currentNick = NickFragment.getInstance().getNickFromEntry();
            }
        }).on("nologin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(NickFragment.getInstance().getNotificationText().getVisibility() == View.INVISIBLE) {
                    NickFragment.getInstance().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NickFragment.getInstance().setNotification(true);
                        }
                    });
                }
                disconnectSocket();
            }
        }).on("userConnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String nick = data.getString("name");
                    ChatlistFragment.getInstance().addViewtoPrivateChatList(nick,
                            new ChatlistEntry(ChatlistFragment.getInstance().getActivity(), nick));
                } catch (JSONException e) {
                    System.out.println("Error getting connected user!");
                }
            }
        }).on("userDisconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    final String nick = data.getString("name");
                    ChatlistFragment.getInstance().removeViewFromPrivateChatList(nick);
                } catch (JSONException e) {
                    System.out.println("Error getting disconnected user!");
                }
            }
        }).on("allUsers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray data = (JSONArray) args[0];
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject user = data.getJSONObject(i);
                        if (user.getString("name").equals(App.getInstance().currentNick)) {

                        } else {
                            String nick = user.getString("name");
                            System.out.println("User: " + nick);
                            ChatlistFragment.getInstance().addViewtoPrivateChatList(nick,
                                    new ChatlistEntry(ChatlistFragment.getInstance().getActivity(), nick));
                        }
                    } catch (JSONException e) {
                        System.out.println("Error getting all users!");

                    }
                }
            }
        }).on("globalmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //TODO Implement global message incoming after chat history / google authentication
            }
        }).on("yourmessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[I] Message was sent successfully");
            }
        });
    }


}
