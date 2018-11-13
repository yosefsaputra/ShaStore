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

public class SecureFileHeaderData implements Parcelable {
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
    private String header;

    protected SecureFileHeaderData(Parcel in) {
    }

    public SecureFileHeaderData(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public String toString() {
        return "SecureFileHeaderData{" +
                "header='" + header + '\'' +
                '}';
    }

    public String toQRCodeString() {
        return String.format("fh: %s", header);
    }

    public boolean toSecureFileHeaderData(String rawValue) {
        Pattern pattern = Pattern.compile("(fh:)([^\\s]*)(\\s*)(.*)");
        Matcher matcher = pattern.matcher(rawValue);

        if (matcher.matches()) {
            header = matcher.group(2);
            return true;
        } else {
            return false;
        }
    }
}