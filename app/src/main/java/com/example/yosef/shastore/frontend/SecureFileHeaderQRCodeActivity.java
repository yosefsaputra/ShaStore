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
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.model.components.QRCodeFactory;
import com.example.yosef.shastore.model.components.SecureFileHeaderData;

public class SecureFileHeaderQRCodeActivity extends AppCompatActivity {
    private static final String TAG = SecureFileHeaderQRCodeActivity.class.getSimpleName();
    public static String ACTION_INTENT_EXTRA = "ACTION_INTENT_EXTRA";
    public static String FILE_ID_INTENT_EXTRA = "FILE_ID_HEADER_INTENT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_file_header_qrcode);

        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_INTENT_EXTRA);
        String secureFileHeader = intent.getStringExtra(FILE_ID_INTENT_EXTRA);

        Log.d(TAG, "action: " + action);
        Log.d(TAG, "secureFileHeader: " + secureFileHeader);

        if (!action.equals("GET_FILE_KEY") || secureFileHeader == null) {
            Toast.makeText(this, "Invalid QR code", Toast.LENGTH_LONG).show();
            finish();
        } else {
            ImageView qrcode_imageview = findViewById(R.id.securefileheaderqrcode_imageview);

            SecureFileHeaderData data = new SecureFileHeaderData(secureFileHeader);

            QRCodeFactory qrCodeFactory = new QRCodeFactory();

            qrcode_imageview.setImageBitmap(qrCodeFactory.generate(data.toQRCodeString()));
        }
    }
}
