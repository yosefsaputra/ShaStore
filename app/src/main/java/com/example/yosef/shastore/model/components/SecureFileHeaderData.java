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
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureFileHeaderData implements Parcelable {
    public static final String TAG = SecureFileHeaderData.class.getSimpleName();

    private String fileId;
    private String cipherKey;
    public static final Creator<SecureFileHeaderData> CREATOR = new Creator<SecureFileHeaderData>() {
        @Override
        public SecureFileHeaderData createFromParcel(Parcel in) {
            return new SecureFileHeaderData(in);
        }

        @Override
        public SecureFileHeaderData[] newArray(int size) {
            return new SecureFileHeaderData[size];
        }
    };

    public SecureFileHeaderData() {
    }

    private String requestDeviceId;

    public SecureFileHeaderData(String fileId, String cipherKey, String requestDeviceId) {
        this.fileId = fileId;
        this.cipherKey = cipherKey;
        this.requestDeviceId = requestDeviceId;
    }

    protected SecureFileHeaderData(Parcel in) {
        fileId = in.readString();
        cipherKey = in.readString();
        requestDeviceId = in.readString();
    }

    public String getRequestDeviceId() {
        return requestDeviceId;
    }

    public void setRequestDeviceId(String requestDeviceId) {
        this.requestDeviceId = requestDeviceId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileId);
        dest.writeString(cipherKey);
        dest.writeString(requestDeviceId);
    }


    public String toQRCodeString() {
        return String.format("a:%s fid:%s ck:%s rdid:%s", TAG, fileId, cipherKey, requestDeviceId);
    }

    public boolean toSecureFileHeaderData(String rawValue) {
        Pattern pattern = Pattern.compile("(a:SecureFileHeaderData)(\\s+)(fid:)([^\\s]*)(\\s+)(ck:)([^\\s]*)(\\s+)(rdid:)([^\\s]*)(\\s*)(.*)");
        Matcher matcher = pattern.matcher(rawValue);

        if (matcher.matches()) {
            Log.i("!!", "matches");
            Log.i("!!", matcher.group(4));
            Log.i("!!", matcher.group(7));
            Log.i("!!", matcher.group(10));
            fileId = matcher.group(4);
            cipherKey = matcher.group(7);
            requestDeviceId = matcher.group(10);
            return true;
        } else {
            return false;
        }
    }

    public String getCipherKey() {
        return cipherKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    @Override
    public String toString() {
        return "SecureFileHeaderData{" +
                "fileId='" + fileId + '\'' +
                ", cipherKey='" + cipherKey + '\'' +
                ", requestDeviceId='" + requestDeviceId + '\'' +
                '}';
    }
}
