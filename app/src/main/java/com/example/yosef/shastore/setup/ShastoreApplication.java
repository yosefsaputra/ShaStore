package com.example.yosef.shastore.setup;

import android.app.Application;
import android.content.Context;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.util.InternalStorageHandler;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class ShastoreApplication extends Application {
    private static String FILE_NAME_INSTANCE_ID = "instanceid";

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
        System.out.println(String.format("INSTANCE ID : %s", readInstanceId()));

        // Initialize Database
        AppDatabase.initializeDatabase(appContext);
        System.out.println(AppDatabase.getDatabase().toString());
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
