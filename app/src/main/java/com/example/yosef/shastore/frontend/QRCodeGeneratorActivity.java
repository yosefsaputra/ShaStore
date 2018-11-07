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
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.yosef.shastore.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGeneratorActivity extends AppCompatActivity {
    public String PASSWORD_HASH_INTENT_EXTRA = "PASSWORD_HASH_INTENT_EXTRA";
    public String DEVICE_UNIQUE_ID_INTENT_EXTRA = "DEVICE_UNIQUE_ID_INTENT_EXTRA";
    public String DEVICE_KEY_INTENT_EXTRA = "DEVICE_KEY_INTENT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_camera);

        Intent intent = getIntent();
        String passwordHash = intent.getStringExtra(PASSWORD_HASH_INTENT_EXTRA);
        String deviceUniqueId = intent.getStringExtra(DEVICE_UNIQUE_ID_INTENT_EXTRA);
        String deviceKey = intent.getStringExtra(DEVICE_KEY_INTENT_EXTRA);

        ImageView qrcode_imageview = findViewById(R.id.qrcode_imageview);
        qrcode_imageview.setImageBitmap(null);
    }

    protected Bitmap generateQrCode(String passwordHash, String deviceUniqueId, String deviceKey) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String text = String.format("p:%s id:%s k:%s",
                passwordHash,
                deviceUniqueId,
                deviceKey
        );
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);
            // TODO: convert BitMatrix to Bitmap
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        // TODO: return the Bitmap
        return null;
    }
}
