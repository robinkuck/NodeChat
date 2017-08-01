package kucki.com.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    public Socket socket;
    public TextView messages;
    public EditText editMsg;
    public Button sendButton;
    public ScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //KEYBOARD OPEN
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        messages = (TextView)findViewById(R.id.MessagesView);
        editMsg = (EditText)findViewById(R.id.editMessage);
        scroller = (ScrollView)findViewById(R.id.scroller);
        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket.connected()) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("text", editMsg.getText());
                        socket.emit("new msg",data);
                        editMsg.setText("");
                    } catch(JSONException e) {
                        System.out.println("Error sending data");
                    }
                }
            }
        });

        connectSocket();
        configSocketEvents();
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
                    final String s1 = messages.getText().toString();
                    final String s2 = data.getString("text");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messages.setText(s1 + "\n\n" + s2);
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

}
