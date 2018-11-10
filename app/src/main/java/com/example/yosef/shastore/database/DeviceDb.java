package com.example.yosef.shastore.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DeviceDb {
    @PrimaryKey
    @NonNull
    private String UUID;

    @NonNull
    private String key;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DeviceDb{" +
                ", name='" + name + '\'' +
                ", UUID='" + UUID + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
