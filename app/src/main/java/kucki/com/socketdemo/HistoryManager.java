package kucki.com.socketdemo;

import android.content.SharedPreferences;

public class HistoryManager  {

    //TODO Implement this feature when Google Authentication is ready!

    private HistoryManager INSTANCE = new HistoryManager();

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private HistoryManager() {
    }

    public HistoryManager getInstance() {
        return INSTANCE;

    }
}
