package com.example.yosef.shastore.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.crypto.SecretKey;

public class SharedPreferenceHandler {
    public static String SHARED_PREFS_CURRENT_PROFILE_SETTINGS = "com.example.yosef.shastore.CURRENT_PROFILE_SETTINGS";
    public static String SHARED_PREFS_CURRENT_PROFILE_USERNAME = "username";


    public static SharedPreferences getSharedPrefsCurrentUserSettings(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_CURRENT_PROFILE_SETTINGS, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPrefsEditorCurrentUserSettings(Context context) {
        return getSharedPrefsCurrentUserSettings(context).edit();
    }

    public static String getSharedPrefsString(Context context) {
        String string = "===== Shared Prefs =====";
        string += "\n";
        string += String.format("%s: %s",
                SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME,
                SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(context).getString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, null));
        string += "\n";
        string += "========================";
        return string;
    }
}
