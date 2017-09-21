package kucki.com.socketdemo.activities;

/**
 * Created by D070264 on 21.09.2017.
 */

public class PrivateChatActivity extends ChatActivity {

    private String sRecipient;

    public PrivateChatActivity(String pRecipient) {
        sRecipient = pRecipient;
    }

    public void setRecipient(String pRecipient) {
        sRecipient = pRecipient;
    }

    public String getRecipient() {
        return sRecipient;
    }

}
