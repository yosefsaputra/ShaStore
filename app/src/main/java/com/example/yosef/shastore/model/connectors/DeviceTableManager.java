package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.components.Profile;

public class DeviceTableManager {
    static int UUIDLength = 25;
    public String generateUUID(String name){
        long mills = System.currentTimeMillis();
        String secs = Long.toString(mills);
        String uuid = name + "T" + secs;
        String more = new String(new char[UUIDLength - uuid.length()]).replace("\0", "#");

        return uuid + more;
    }
    public boolean isKnown(String uuid){
        Device prev = AppDatabase.getDatabase().getDevicebyId(uuid);
        if (prev == null) return false;
            else return true;
    }
    public boolean saveDevice(String uuid, String key){
        Device newD = new Device(uuid, key);
        return AppDatabase.getDatabase().addDevice(newD);
    }
}
