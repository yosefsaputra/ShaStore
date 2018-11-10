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
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.components.FileObject;
import com.example.yosef.shastore.model.components.RegularFile;
import com.example.yosef.shastore.model.connectors.ByteCrypto;
import com.example.yosef.shastore.model.util.InternalStorageHandler;
import com.example.yosef.shastore.model.util.SharedPreferenceHandler;
import com.example.yosef.shastore.setup.ShastoreApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_REQUEST_CODE = 40;
    private static final int ENCRYPT_REQUEST_CODE = 41;
    private static final int DECRYPT_REQUEST_CODE = 42;

    private static EditText plainFileName;
    private static EditText secureFileName;
    private static SecretKey savedKey;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RegularFile plainFile = new RegularFile();
    private EncryptedFile secureFile = new EncryptedFile();
    private String action = "null";

    private ConstraintLayout layout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.main_activity_layout);
        configureNavigationDrawer();
        configureToolbar();
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(android.R.drawable.ic_menu_preferences);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        NavigationView navView = findViewById(R.id.main_activity_navigation_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_devices: {
                        Intent intent = new Intent(getApplicationContext(), RegisteredDevicesActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_qrcode: {
                        Intent intent = new Intent(getApplicationContext(), QRCodeGeneratorActivity.class);

                        String passwordHash = AppDatabase.getDatabase().getProfile(
                                SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(getApplicationContext()).getString(
                                        SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME,
                                        null
                                )
                        ).getPasswordHash();

                        String instanceId = null;
                        try {
                            instanceId = new String(InternalStorageHandler.readFile(getApplicationContext(), ShastoreApplication.FILE_NAME_INSTANCE_ID, new byte[16]), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        intent.putExtra(QRCodeGeneratorActivity.PASSWORD_HASH_INTENT_EXTRA, passwordHash);
                        intent.putExtra(QRCodeGeneratorActivity.DEVICE_UNIQUE_ID_INTENT_EXTRA, instanceId);
                        intent.putExtra(QRCodeGeneratorActivity.DEVICE_KEY_INTENT_EXTRA, instanceId);
                        startActivity(intent);
                        break;
                    }
                    case R.id.debug_reset_database: {
                        AppDatabase.getDatabase().resetDatabase();
                    }
                    case R.id.menu_sign_out: {
                        SharedPreferenceHandler.getSharedPrefsEditorCurrentUserSettings(getApplicationContext()).putString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, null).apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    default: {
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // manage other entries if you have it ...
        }
        return true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        plainFileName = findViewById(R.id.plainFileName);
        secureFileName = findViewById(R.id.secureFileName);
    }
    public void newFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("text/plain");
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt");

        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    private void writeFileContent(Uri uri)
    {
        try{
            ParcelFileDescriptor pfd =
                    this.getContentResolver().
                            openFileDescriptor(uri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            String textContent =
                    plainFileName.getText().toString();

            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    private void readFileFromUri(Uri uri, FileObject newFile) throws IOException{
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

    private void doEncryption(FileObject plainFile, Uri secureUri)  {
        try{
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            savedKey = keygen.generateKey();
            secureFile = new EncryptedFile();
            byte[] cipherText = ByteCrypto.encryptByte(plainFile.getContent(), savedKey);
            ParcelFileDescriptor pfd =
                    this.getContentResolver().
                            openFileDescriptor(secureUri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            secureFile.setContent(cipherText);
            secureFile.writeContent(fileOutputStream);
            pfd.close();
            Log.d(TAG, "Saving encrypted file" );
        } catch (NoSuchAlgorithmException e){

        } catch (IOException e) {
            Log.e(TAG, "Cannot open file " + secureUri.toString());
        }

    }

    private void doDecryption(FileObject secureFile, Uri plainUri){
        try{
//            DocumentsContract.deleteDocument(getContentResolver(), plainUri);
//            return;
            plainFile = new RegularFile();
            byte[] plainText = ByteCrypto.decryptByte(secureFile.getContent(), savedKey);
            ParcelFileDescriptor pfd = this.getContentResolver().
                            openFileDescriptor(plainUri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            plainFile.setContent(plainText);
            plainFile.writeContent(fileOutputStream);
            pfd.close();
            Log.d(TAG,"Saving plain file ");
        } catch (IOException e) {
            Log.e(TAG, "Cannot open file " + plainUri.toString());
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        Uri currentUri = null;

        if (resultCode == Activity.RESULT_OK)
        {

            if (requestCode == ENCRYPT_REQUEST_CODE)
            {

                if (resultData != null) {
                    currentUri = resultData.getData();
                    try {
                        readFileFromUri(currentUri, plainFile);
                        plainFileName.setText(plainFile.getName());
                        action = "enc";
                    } catch (Exception e) {
                        // Handle error here
                    }
                }
            }
            if (requestCode == DECRYPT_REQUEST_CODE)
            {

                if (resultData != null) {
                    currentUri = resultData.getData();
                    try {
                        readFileFromUri(currentUri, secureFile);
                        secureFileName.setText(secureFile.getName());
                        action = "dec";
                        //Toast.makeText(this, new String(secureFile.getContent()), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Open secure file !!!!!! " + new String(secureFile.getContent()));
                    } catch (Exception e) {
                        // Handle error here
                    }
                }
            }
            if (requestCode == CREATE_REQUEST_CODE)
            {

                if (resultData != null) {
                    currentUri = resultData.getData();
                    try {

                        if (action == "enc"){
                            Log.i(TAG, "Create a encrypted file");
                            doEncryption(plainFile, currentUri);
                        }else{
                            Log.i(TAG, "Create a plain file");
                            doDecryption(secureFile, currentUri);
                        }

                        //secureFileName.setText(secureFile.getName());
                    } catch (Exception e) {
                        // Handle error here
                    }
                }
            }
        }
    }
    public void decryptFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("text/plain");
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, DECRYPT_REQUEST_CODE);
    }

    public void encryptFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("*/*");
        startActivityForResult(intent, ENCRYPT_REQUEST_CODE);
    }

    public void saveFile(View view){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("text/plain");
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }
}
