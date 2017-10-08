package kucki.com.socketdemo.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.ScrollView;

import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kucki.com.socketdemo.ActivityManager;
import kucki.com.socketdemo.App;
import kucki.com.socketdemo.MessageView;
import kucki.com.socketdemo.R;

import static android.widget.LinearLayout.HORIZONTAL;

public class ChatActivity extends AppCompatActivity {

    public EditText editMsg;
    public Button sendButton;
    public ScrollView scroller;
    public LinearLayout msgs;

    public int x,y;

    //TODO: Activity for global chat and for private chat(multiple chat objects possible)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        App.current = this;

        //KEYBOARD OPEN
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //messages = (TextView)findViewById(R.id.MessagesView);
        configViews();
        msgs = (LinearLayout) findViewById(R.id.rel);

        Display d = getWindowManager().getDefaultDisplay();
        x = d.getWidth();
        y = d.getHeight();
    }

    @Override
    public void onBackPressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }).start();
        return true;
    }

    public void configViews() {
        editMsg = (EditText)findViewById(R.id.editMessage);
        scroller = (ScrollView)findViewById(R.id.scroller);
        sendButton = (Button)findViewById(R.id.sendButton);
        /*
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.getSocket().connected()&&!editMsg.getText().toString().equals("")) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("text", editMsg.getText());
                        App.getSocket().emit("new msg",data);


                        //addPersonalMessageView(editMsg.getText().toString());
                        clearEditMsg();
                        scrollDown();
                    } catch(JSONException e) {
                        System.out.println("Error sending data");
                    }
                }
            }
        });
        */
    }

    //TODO: Add Writing Display
    //TODO: Add reading checkmark

    /*
    public void configSocketEvents() {
        App.getSocket().on("message",new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //final String s1 = messages.getText().toString();
                    final String s2 = data.getString("text");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            addMessageView(s2);
                            scrollDown();
                        }
                    });
                }catch(JSONException e){
                    System.out.println("Error getting new message!");
                }
            }
        }).on("yourmessage",new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //final String s1 = messages.getText().toString();
                    final String s2 = data.getString("text");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addPersonalMessageView(s2);
                            scrollDown();
                        }
                    });
                }catch(JSONException e){
                    System.out.println("Error getting new message!");
                }
            }
        });
    }
    */

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

        MessageView mv = new MessageView(this,text,(int)((x/3)*2), false);
        mv.setBackgroundResource(R.drawable.bgpersonalmessageview);
        l.addView(mv);
        msgs.addView(l);
        addDate(mv);
    }

    public void clearEditMsg() {
        editMsg.setText("");
    }

    public void addDate(MessageView mv) {
        BadgeView badgeView = new BadgeView(this, mv);
        badgeView.setText(getCurrentTime());
        badgeView.setTextColor(getResources().getColor(R.color.date));
        badgeView.setTextSize((int)dpToPixel(4));
        badgeView.setBadgeBackgroundColor(Color.BLACK);
        badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_LEFT);
        //badgeView.setPadding((int)dpToPixel(2),(int)dpToPixel(2),(int)dpToPixel(2),(int)dpToPixel(2));
        //badgeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.linemessageview));
        //badgeView.setBadgeMargin((int)dpToPixel(3),(int)dpToPixel(3));
        badgeView.show();
    }

    //Messages from the server
    public MessageView createMessageView(String text, boolean global) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(HORIZONTAL);
        l.setHorizontalGravity(Gravity.LEFT);

        MessageView mv = new MessageView(this,text,(int)((x/3)*2), global);
        l.addView(mv);
        msgs.addView(l);
        //addDate(mv);
        return mv;
    }

    public String getCurrentTime() {
        Calendar c = Calendar.getInstance(Locale.GERMANY);
        String date = "";
        if(c.get(Calendar.DAY_OF_MONTH)<10) {
            date = "0" + c.get(Calendar.DAY_OF_MONTH) + "." +
                    (c.get(Calendar.MONTH)<10 ? "0" + c.get(Calendar.MONTH) : c.get(Calendar.MONTH)) +
                    "." + c.get(Calendar.YEAR);
        } else {
            date =  c.get(Calendar.DAY_OF_MONTH) + "." +
                    (c.get(Calendar.MONTH)<10 ? "0" + c.get(Calendar.MONTH) : c.get(Calendar.MONTH)) +
                    "." + c.get(Calendar.YEAR);
        }

        String time = "";
        if(c.get(Calendar.MINUTE) < 10) {
            time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
        } else {
            time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        }
        return date + ", " + time;
    }

    //DP to Pixel
    public float dpToPixel(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
        /*
        final float density = getResources().getDisplayMetrics().density;
        return dp * (density == 1.0f || density == 1.5f || density == 2.0f ? 3.0f : density) + 0.5f;
        */
    }
}
