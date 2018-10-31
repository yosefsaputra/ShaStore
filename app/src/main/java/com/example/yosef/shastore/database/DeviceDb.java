package com.example.yosef.shastore.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DeviceDb {
    @PrimaryKey(autoGenerate = true)
    private long dbid;

    private String name;
    private String friendlyName;
    private String UUID;
    private String key;
}
