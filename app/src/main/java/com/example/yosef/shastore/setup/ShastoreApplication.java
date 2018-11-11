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
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.connectors.ByteCrypto;
import com.example.yosef.shastore.model.util.InternalStorageHandler;

import java.util.UUID;

public class ShastoreApplication extends Application {
    private static String TAG = ShastoreApplication.class.getSimpleName();

    public static String FILE_NAME_INSTANCE_ID = "instanceid";

    public static String instanceId;

    @Override
    public void onCreate() {
        super.onCreate();

        initialization();
    }

    private void initialization() {
        Context appContext = getApplicationContext();

        // Initialize Database
        AppDatabase.initializeDatabase(appContext);
        Log.i(TAG, AppDatabase.getDatabase().toString());

        // Check the instance ID
        boolean instanceIdCheck = checkInstanceId();

        // If no instance ID, create instance ID
        if (!instanceIdCheck) {
            createInstanceId();
        }

        instanceId = readInstanceId();

        Log.i(TAG, String.format("INSTANCE ID : %s", readInstanceId()));
    }

    private boolean checkInstanceId() {
        boolean res1 = InternalStorageHandler.checkFile(getApplicationContext(), FILE_NAME_INSTANCE_ID);
        if (!res1) {
            return false;
        }

        return !(AppDatabase.getDatabase().getDevicebyId(readInstanceId()) == null);
    }

    private boolean createInstanceId() {
        String instanceId = UUID.randomUUID().toString().replace("-", "");

        Log.i(TAG, String.format("Instance ID - create - String: %s", instanceId));

        boolean res1 = AppDatabase.getDatabase().addDevice(new Device(instanceId, ByteCrypto.key2Str(ByteCrypto.generateRandKey())));
        boolean res2 = InternalStorageHandler.createFile(getApplicationContext(), FILE_NAME_INSTANCE_ID, ByteCrypto.str2Byte(instanceId));

        return res1 && res2;
    }

    private String readInstanceId() {
        return ByteCrypto.byte2Str(InternalStorageHandler.readFile(getApplicationContext(), FILE_NAME_INSTANCE_ID)).replaceAll("\n", "");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
