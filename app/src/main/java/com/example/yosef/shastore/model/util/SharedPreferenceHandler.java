package com.example.yosef.shastore.model.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHandler {
    public static String SHARED_PREFS_CURRENT_USER_SETTINGS = "edu.utexas.mpc.warble3.CURRENT_USER_SETTINGS";
    public static String SHARED_PREFS_USERNAME = "";

    public static SharedPreferences getSharedPrefsCurrentUserSettings(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_CURRENT_USER_SETTINGS, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPrefsEditorCurrentUserSettings(Context context) {
        return getSharedPrefsCurrentUserSettings(context).edit();
    }

    public static String getSharedPrefsString(Context context) {
        String string = "===== Shared Prefs =====";
        string += "\n";
        string += String.format("%s: %s", SharedPreferenceHandler.SHARED_PREFS_USERNAME, SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(context).getString(SharedPreferenceHandler.SHARED_PREFS_USERNAME, null));
        string += "\n";
        string += "========================";
        return string;
    }
}