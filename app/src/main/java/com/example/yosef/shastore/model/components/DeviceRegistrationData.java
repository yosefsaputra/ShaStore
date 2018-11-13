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

package com.example.yosef.shastore.model.components;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceRegistrationData implements Parcelable {
    public static final Creator<DeviceRegistrationData> CREATOR = new Creator<DeviceRegistrationData>() {
        @Override
        public DeviceRegistrationData createFromParcel(Parcel in) {
            return new DeviceRegistrationData(in);
        }

        @Override
        public DeviceRegistrationData[] newArray(int size) {
            return new DeviceRegistrationData[size];
        }
    };
    private String passwordHash;
    private String deviceUniqueId;
    private String deviceKey;

    public DeviceRegistrationData() {
    }

    public DeviceRegistrationData(String passwordHash, String deviceUniqueId, String deviceKey) {
        this.passwordHash = passwordHash;
        this.deviceUniqueId = deviceUniqueId;
        this.deviceKey = deviceKey;
    }

    protected DeviceRegistrationData(Parcel in) {
        passwordHash = in.readString();
        deviceUniqueId = in.readString();
        deviceKey = in.readString();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDeviceUniqueId() {
        return deviceUniqueId;
    }

    public void setDeviceUniqueId(String deviceUniqueId) {
        this.deviceUniqueId = deviceUniqueId;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @Override
    public String toString() {
        return "DeviceRegistrationData{" +
                "passwordHash='" + passwordHash + '\'' +
                ", deviceUniqueId='" + deviceUniqueId + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                '}';
    }

    public String toQRCodeString() {
        return String.format("p:%s id:%s k:%s",
                passwordHash,
                deviceUniqueId,
                deviceKey
        );
    }

    public boolean toDeviceRegistrationData(String rawValue) {
        Pattern pattern = Pattern.compile("(p:)([^\\s]*)(\\s+)(id:)([^\\s]*)(\\s+)(k:)([^\\s]*)(\\s*)(.*)");
        Matcher matcher = pattern.matcher(rawValue);

        if (matcher.matches()) {
            passwordHash = matcher.group(2);
            deviceUniqueId = matcher.group(5);
            deviceKey = matcher.group(8);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(passwordHash);
        dest.writeString(deviceUniqueId);
        dest.writeString(deviceKey);
    }
}
