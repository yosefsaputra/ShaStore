package com.example.yosef.shastore.setup;

import android.app.Application;
import android.content.Context;

import com.example.yosef.shastore.database.AppDatabase;

public class ShastoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initialization();
    }

    private void initialization() {
        Context appContext = getApplicationContext();

        AppDatabase.initializeDatabase(appContext);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
