/*
 * Copyright 2018 Shastore Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.yosef.shastore.model.util;

import android.content.Context;
import android.content.SharedPreferences;

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
                SHARED_PREFS_CURRENT_PROFILE_USERNAME,
                SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(context).getString(SHARED_PREFS_CURRENT_PROFILE_USERNAME, null));
        string += "\n";
        string += "========================";
        return string;
    }
}