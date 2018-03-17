package de.robinkuck.nodechat.android.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class NickManager {

    private static NickManager INSTANCE;
    private String currentNick = "";

    private NickManager() {

    }

    public static NickManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new NickManager();
        }
        return INSTANCE;
    }

    public String getCurrentNick() {
        if (currentNick.equals("")) {
            currentNick = CustomActivityManager.getInstance().getCurrentActivity().
                    getSharedPreferences("nodechat", Context.MODE_PRIVATE).
                    getString("nickName", "");
        }
        return currentNick;
    }

    public void setCurrentNick(final String currentNick) {
        this.currentNick = currentNick;
        SharedPreferences preferences = CustomActivityManager.getInstance().getCurrentActivity().getSharedPreferences("nodechat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nickName", currentNick);
        editor.commit();
        System.out.println("[NickManager] nick name successfully updated");
    }

}
