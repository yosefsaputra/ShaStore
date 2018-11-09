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

    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
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
                "dbid=" + dbid +
                ", name='" + name + '\'' +
                ", friendlyName='" + friendlyName + '\'' +
                ", UUID='" + UUID + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
