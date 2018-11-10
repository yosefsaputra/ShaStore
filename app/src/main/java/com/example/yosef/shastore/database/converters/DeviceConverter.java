package com.example.yosef.shastore.database.converters;

import com.example.yosef.shastore.database.DeviceDb;
import com.example.yosef.shastore.model.components.Device;

public class DeviceConverter {
    public static Device toDevice(DeviceDb deviceDb){
        if (deviceDb == null) return null;
        Device newD = new Device(deviceDb.getUUID(), deviceDb.getKey());
        newD.setName(deviceDb.getName());
        return newD;
    }
    public static DeviceDb toDeviceDb(Device device){
        if (device == null) return null;
        DeviceDb newD = new DeviceDb();
        newD.setKey(device.getKey());
        newD.setUUID(device.getUUID());
        newD.setName(device.getName());
        return newD;
    }
}
