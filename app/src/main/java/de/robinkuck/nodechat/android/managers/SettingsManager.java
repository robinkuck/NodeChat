package de.robinkuck.nodechat.android.managers;

/**
 * Created by D070264 on 09.02.2018.
 */

/**
 * This class loads and saves settings that have been made in SettingsActivity
 * e.g. notifications
 */
public class SettingsManager {

    private static SettingsManager INSTANCE;

    private boolean global_message_notification;

    private SettingsManager() {
        loadSettingsFromPreferences();
    }

    public static SettingsManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingsManager();
        }
        return INSTANCE;
    }

    private void loadSettingsFromPreferences() {

    }

}

