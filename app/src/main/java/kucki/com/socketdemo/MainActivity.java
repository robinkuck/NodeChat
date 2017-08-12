package kucki.com.socketdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    public Socket socket;
    //public TextView messages;
    public EditText editMsg;
    public Button sendButton;
    public ScrollView scroller;
    public LinearLayout msgs;

    public int x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //KEYBOARD OPEN
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //messages = (TextView)findViewById(R.id.MessagesView);
        editMsg = (EditText)findViewById(R.id.editMessage);
        scroller = (ScrollView)findViewById(R.id.scroller);
        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket.connected()&&!editMsg.getText().toString().equals("")) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("text", editMsg.getText());
                        socket.emit("new msg",data);


                        addPersonalMessageView(editMsg.getText().toString());
                        clearEditMsg();
                        scrollDown();
                    } catch(JSONException e) {
                        System.out.println("Error sending data");
                    }
                }
            }
        });
        msgs = (LinearLayout) findViewById(R.id.rel);


        connectSocket();
        configSocketEvents();

        Display d = getWindowManager().getDefaultDisplay();
        x = d.getWidth();
        y = d.getHeight();
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://37.10.112.240:8010/");
            socket.connect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void configSocketEvents() {
        final Context ct = this;
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                System.out.println("SocketIO connected!");
            }
        }).on("message",new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //final String s1 = messages.getText().toString();
                    final String s2 = data.getString("text");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*
                            messages.setText(s1 + "\n\n" + s2);
                            scrollDown();
                            */
                            addMessageView(s2);
                            scrollDown();
                        }
                    });
                }catch(JSONException e){
                    System.out.println("Error getting new message!");
                }
            }
        });
    }

    public void scrollDown() {
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
                //scroller.scrollTo(0,scroller.getBottom());
            }
        });
    }

    //socket.emit(EVENT, data);

    //Messages from the client
    public void addPersonalMessageView(String text) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.RIGHT);

        MessageView mv = new MessageView(this,text,(int)((x/3)*2));
        mv.setBackgroundResource(R.drawable.bgpersonalmessageview);
        l.addView(mv);
        msgs.addView(l);
    }

    //Messages from the server
    public void addMessageView(String text) {
        MessageView mv = new MessageView(this,text,(int)((x/3)*2));
        msgs.addView(mv);
    }

    public void clearEditMsg() {
        editMsg.setText("");
    }

}
