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

package com.example.yosef.shastore.setup;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.util.InternalStorageHandler;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class ShastoreApplication extends Application {
    private static String TAG = ShastoreApplication.class.getSimpleName();

    public static String FILE_NAME_INSTANCE_ID = "instanceid";

    @Override
    public void onCreate() {
        super.onCreate();

        initialization();
    }

    private void initialization() {
        Context appContext = getApplicationContext();

        // Check the instance ID
        boolean instanceIdCheck = checkInstanceId();

        // If no instance ID, create instance ID
        if (!instanceIdCheck) {
            createInstanceId();
        }
        Log.i(TAG, String.format("INSTANCE ID : %s", readInstanceId()));

        // Initialize Database
        AppDatabase.initializeDatabase(appContext);
        Log.i(TAG, AppDatabase.getDatabase().toString());
    }

    private boolean checkInstanceId() {
        return InternalStorageHandler.checkFile(getApplicationContext(), FILE_NAME_INSTANCE_ID);
    }

    private boolean createInstanceId() {
        return InternalStorageHandler.createFile(getApplicationContext(), FILE_NAME_INSTANCE_ID, UUID.randomUUID().toString().getBytes());
    }

    private String readInstanceId() {
        try {
            return new String(InternalStorageHandler.readFile(getApplicationContext(), FILE_NAME_INSTANCE_ID, new byte[16]), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
