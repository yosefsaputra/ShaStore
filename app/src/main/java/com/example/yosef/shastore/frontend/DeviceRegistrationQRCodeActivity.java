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

package com.example.yosef.shastore.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.model.components.DeviceRegistrationData;
import com.example.yosef.shastore.model.components.QRCodeFactory;

public class DeviceRegistrationQRCodeActivity extends AppCompatActivity {
    public static String ACTION_INTENT_EXTRA = "ACTION_INTENT_EXTRA";
    public static String PASSWORD_HASH_INTENT_EXTRA = "PASSWORD_HASH_INTENT_EXTRA";
    public static String DEVICE_UNIQUE_ID_INTENT_EXTRA = "DEVICE_UNIQUE_ID_INTENT_EXTRA";
    public static String DEVICE_KEY_INTENT_EXTRA = "DEVICE_KEY_INTENT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_camera);

        Intent intent = getIntent();
        String passwordHash = intent.getStringExtra(PASSWORD_HASH_INTENT_EXTRA);
        String deviceUniqueId = intent.getStringExtra(DEVICE_UNIQUE_ID_INTENT_EXTRA);
        String deviceKey = intent.getStringExtra(DEVICE_KEY_INTENT_EXTRA);

        if (passwordHash == null || deviceUniqueId == null || deviceKey == null) {
            Toast.makeText(this, "Invalid QR code", Toast.LENGTH_LONG).show();
            finish();
        } else {
            ImageView qrcode_imageview = findViewById(R.id.qrcode_imageview);

            DeviceRegistrationData data = new DeviceRegistrationData();
            data.setPasswordHash(passwordHash);
            data.setDeviceUniqueId(deviceUniqueId);
            data.setDeviceKey(deviceKey);

            QRCodeFactory qrCodeFactory = new QRCodeFactory();

            qrcode_imageview.setImageBitmap(qrCodeFactory.generate(data.toQRCodeString()));
        }
    }
}
