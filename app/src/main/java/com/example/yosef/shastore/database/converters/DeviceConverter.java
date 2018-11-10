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

package com.example.yosef.shastore.database.converters;


import com.example.yosef.shastore.database.DeviceDb;
import com.example.yosef.shastore.model.components.Device;

public class DeviceConverter {
    public static Device toDevice(DeviceDb deviceDb) {
        if (deviceDb == null) {
            return null;
        }

        Device device = new Device();

        device.setName(deviceDb.getName());
        device.setFriendlyName(deviceDb.getName());
        device.setUUID(deviceDb.getUUID());
        device.setKey(deviceDb.getKey());

        return device;
    }

    public static DeviceDb toDeviceDb(Device device) {
        if (device == null) {
            return null;
        }

        DeviceDb deviceDb = new DeviceDb();

        deviceDb.setName(device.getName());
        deviceDb.setUUID(device.getUUID());
        deviceDb.setKey(device.getKey());

        return deviceDb;
    }
}
