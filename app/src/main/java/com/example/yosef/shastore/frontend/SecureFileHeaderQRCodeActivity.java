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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.components.FileObject;
import com.example.yosef.shastore.model.components.QRCodeFactory;
import com.example.yosef.shastore.model.components.SecureFileHeaderData;
import com.example.yosef.shastore.model.connectors.ByteCrypto;
import com.example.yosef.shastore.setup.ShastoreApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SecureFileHeaderQRCodeActivity extends AppCompatActivity {
    private static final String TAG = SecureFileHeaderQRCodeActivity.class.getSimpleName();
    public static final String ACTION_INTENT_EXTRA = "ACTION_INTENT_EXTRA";
    public static final String FILE_ID_INTENT_EXTRA = "FILE_ID_INTENT_EXTRA";
    public static final String FILE_CIPHER_KEY_INTENT_EXTRA = "FILE_CIPHER_KEY_INTENT_EXTRA";
    public static final String FILE_HEADER_RETURN_INTENT_EXTRA = "FILE_HEADER_RETURN_INTENT_EXTRA";
    private static final int CHOOSE_FILE = 41;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_file_header_qrcode);

        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_INTENT_EXTRA);
        String secureFileId = intent.getStringExtra(FILE_ID_INTENT_EXTRA);
        String secureFileCipherKey = ByteCrypto.byte2Str(intent.getByteArrayExtra(FILE_CIPHER_KEY_INTENT_EXTRA));

        Log.d(TAG, "action: " + action);
        Log.d(TAG, "secureFileId: " + secureFileId);
        Log.d(TAG, "secureFileCipherKey: " + secureFileCipherKey);

        if (!action.equals("GET_FILE_HEADER") || secureFileId == null || secureFileCipherKey == null) {
            Toast.makeText(this, "Invalid QR code", Toast.LENGTH_LONG).show();
            finish();
        } else {
            ImageView qrcode_imageview = findViewById(R.id.securefileheaderqrcode_imageview);

            SecureFileHeaderData data = new SecureFileHeaderData(secureFileId, secureFileCipherKey, ShastoreApplication.instanceId);

            QRCodeFactory qrCodeFactory = new QRCodeFactory();

            qrcode_imageview.setImageBitmap(qrCodeFactory.generate(data.toQRCodeString()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                if (resultData != null) {
                    EncryptedFile txte = new EncryptedFile();
                    try {
                        readFileFromUri(resultData.getData(), txte);
                        // TODO: add back
                        // DocumentsContract.deleteDocument(getContentResolver(), resultData.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Device device = AppDatabase.getDatabase().getDevicebyId(ShastoreApplication.instanceId);

                    if (device == null) {
                        Toast.makeText(this, "Unregistered Device", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.i(TAG, Arrays.toString(txte.getCipherKey()));
                    Log.i(TAG, txte.getFileId());

                    byte[] fileKey = ByteCrypto.decryptByte(txte.getCipherKey(), ByteCrypto.str2Key(device.getKey()));
                    txte.setFileKey(ByteCrypto.byte2key(fileKey));
                    Log.i(TAG, ByteCrypto.key2Str(txte.getFileKey()));
                    AppDatabase.getDatabase().addEncFile(txte);
                    Toast.makeText(this,"Saved file key to database", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        }
    }

    private void readFileFromUri(Uri uri, FileObject newFile) throws IOException {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                newFile.setName(displayName);
            }
        } finally {
            cursor.close();
        }
        InputStream inputStream = getContentResolver().openInputStream(uri);
        newFile.readContent(inputStream);
    }

    public void chooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("text/plain");
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CHOOSE_FILE);
    }
}
