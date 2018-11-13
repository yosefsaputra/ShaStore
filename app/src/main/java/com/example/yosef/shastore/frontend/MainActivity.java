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
import android.widget.Toast;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.components.DeviceRegistrationData;
import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.components.FileObject;
import com.example.yosef.shastore.model.components.RegularFile;
import com.example.yosef.shastore.model.connectors.ByteCrypto;
import com.example.yosef.shastore.model.connectors.DeviceTableManager;
import com.example.yosef.shastore.model.connectors.FileTableManager;
import com.example.yosef.shastore.model.connectors.ProfileManager;
import com.example.yosef.shastore.model.util.InternalStorageHandler;
import com.example.yosef.shastore.model.util.SharedPreferenceHandler;
import com.example.yosef.shastore.setup.ShastoreApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    private static final int CREATE_REQUEST_CODE = 40;
    private static final int ENCRYPT_REQUEST_CODE = -1;
    private static final int DECRYPT_REQUEST_CODE = -1;
    private static final int REGISTER_DEVICE = 50;
    private static final int CHOOSE_FILE = 41;
    private static final int GET_FILE_HEADER = 42;


    private static EditText fileName;
    private static SecretKey savedKey;
    private Uri currentUri;
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
                        Intent intent = new Intent(getApplicationContext(), DeviceRegistrationQRCodeActivity.class);

                        String passwordHash = AppDatabase.getDatabase().getProfile(
                                SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(getApplicationContext()).getString(
                                        SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME,
                                        null
                                )
                        ).getPasswordHash();

                        String instanceId = ShastoreApplication.instanceId;
                        String deviceKey = null;
                        try {
                            deviceKey = AppDatabase.getDatabase().getDevicebyId(instanceId).getKey();
                        } catch (NullPointerException e) {
                        }

                        intent.putExtra(DeviceRegistrationQRCodeActivity.PASSWORD_HASH_INTENT_EXTRA, passwordHash);
                        intent.putExtra(DeviceRegistrationQRCodeActivity.DEVICE_UNIQUE_ID_INTENT_EXTRA, instanceId);
                        intent.putExtra(DeviceRegistrationQRCodeActivity.DEVICE_KEY_INTENT_EXTRA, deviceKey);
                        startActivity(intent);
                        break;
                    }
                    case R.id.debug_reset_database: {
                        AppDatabase.getDatabase().resetDatabase();
                        InternalStorageHandler.deleteFile(getApplicationContext(), ShastoreApplication.FILE_NAME_INSTANCE_ID);
                        getApplication().onCreate();
                    }
                    case R.id.menu_sign_out: {
                        SharedPreferenceHandler.getSharedPrefsEditorCurrentUserSettings(getApplicationContext()).putString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, null).apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.debug_print_database: {
                        String[] databaseStrings = AppDatabase.getDatabase().toString().split("\n");
                        for (String string : databaseStrings) {
                            Log.i(TAG, string);
                        }
                        break;
                    }
                    default: {
                    }
                }
                return true;
            }
        });
    }

    public void openRegistrationCameraActivity(View view) {
        Intent intent = new Intent(this, RegistrationCameraActivity.class);
        startActivityForResult(intent, REGISTER_DEVICE);
    }

    public boolean registerDevice(Device device) {
        return AppDatabase.getDatabase().addDevice(device);
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
        fileName = findViewById(R.id.fileName);
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

    private String getFileNameFromUri(Uri uri){
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                return displayName;
            }
        } finally {
            cursor.close();
        }
        return "";
    }

    private void doEncryption(FileObject plainFile, Uri secureUri)  {
        try{
            SecretKey nowKey = ByteCrypto.generateRandKey();

            secureFile = new EncryptedFile();
            byte[] cipherText = ByteCrypto.encryptByte(plainFile.getContent(), nowKey);
            secureFile.setContent(cipherText);
            secureFile.setFileKey(nowKey);
            secureFile.setFileId(EncryptedFile.generateFileId(ShastoreApplication.instanceId));
            SecretKey deviceKey = new DeviceTableManager().getKey(ShastoreApplication.instanceId);
            //Log.i("!!!!!!!!", ByteCrypto.key2Str(deviceKey));

            byte[] cipherKey = ByteCrypto.encryptByte(nowKey.getEncoded(), deviceKey);
            secureFile.setCipherKey(cipherKey);
            new FileTableManager().saveFile(secureFile);

            // add head

            ParcelFileDescriptor pfd =
                    this.getContentResolver().
                            openFileDescriptor(secureUri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());


            secureFile.writeContent(fileOutputStream);
            pfd.close();
            Toast.makeText(this, "Saved encrypted file to cloud", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Cannot open file " + secureUri.toString());
        }

    }

    private void doDecryption(EncryptedFile secureFile, Uri plainUri){
        try{
//            DocumentsContract.deleteDocument(getContentResolver(), plainUri);
//            return;

            plainFile = new RegularFile();

            byte[] plainText = ByteCrypto.decryptByte(secureFile.getContent(), secureFile.getFileKey());
            ParcelFileDescriptor pfd = this.getContentResolver().
                            openFileDescriptor(plainUri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            plainFile.setContent(plainText);
            plainFile.writeContent(fileOutputStream);
            pfd.close();
            Log.d(TAG,"Saving plain file ");
            Toast.makeText(this, "Saved plain file to local storage", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Cannot open file " + plainUri.toString());
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {


        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CHOOSE_FILE) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    fileName.setText(getFileNameFromUri(currentUri));
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
            if (requestCode == REGISTER_DEVICE) {
                DeviceRegistrationData deviceRegistrationData = resultData.getParcelableExtra(RegistrationCameraActivity.DATA);

                boolean registered = false;

                // create a new device
                Device newDevice = null;

                // validate the passwordHash
                ProfileManager profileManager = new ProfileManager();
                String username = SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(this).getString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, null);
                if (profileManager.authenticateProfile(username, deviceRegistrationData.getPasswordHash())) {
                    newDevice = new Device();
                    newDevice.setUUID(deviceRegistrationData.getDeviceUniqueId());
                    newDevice.setKey(deviceRegistrationData.getDeviceKey());
                }

                // register the new device to database
                if ((newDevice != null) && registerDevice(newDevice)) {
                    registered = true;
                }

                String toast;
                if (!registered) {
                    toast = "Device Registration is unsuccessful";
                } else {
                    toast = "Device Registration is successful";
                }
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            }

            if (requestCode == GET_FILE_HEADER) {
                Log.i(TAG, ByteCrypto.byte2Str(resultData.getByteArrayExtra(SecureFileHeaderQRCodeActivity.FILE_HEADER_RETURN_INTENT_EXTRA)));
                // TODO: implement
                // 1. Decrypt the file header with current device key
                // 2. Get the fileKey
                // 3. Put the filekey to the database
            }
        }
    }
    public void saveFile(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("text/plain");
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }
    public void chooseFile(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("text/plain");
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CHOOSE_FILE);
    }

    public void encryptFile(View view){
        try{
            readFileFromUri(currentUri, plainFile);
            action = "enc";
            saveFile();
        } catch (IOException e){
            Log.e(TAG, "File IO Exception");
        }
    }

    public void decryptFile(View view){
        try{
            readFileFromUri(currentUri, secureFile);
            action = "dec";
            secureFile.setFileKey(new FileTableManager().getFileKey(secureFile.getFileId()));
            if (secureFile.getFileKey() != null){
                saveFile();
            } else {
                //TODO: @Yosef you shuold implement device B code here;
                // Here you can use the secureFile to access the encrypted file user chose.
                // In the secureFile the fileId is the id of the file, and the cipherKey is the fileKey after encryption.
                // the fileId is (DeviceId+#T#+Time) in Base64String. So the last character of fileId maybe random.

                Log.i(TAG, "File ID: " + secureFile.getFileId());

                String deviceId = EncryptedFile.getDeviceId(secureFile.getFileId());
                if (deviceId == null) {
                    Toast.makeText(this, "Invalid ShaStore File", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i(TAG, "Device ID: " + deviceId);
                Device thisDevice =AppDatabase.getDatabase().getDevicebyId(deviceId);
                if ( thisDevice!= null) {
                    // TODO : implement
                    // 1. Get the device key from database based on device id
                    // 2. Decrypt file header with device key
                    // 3. Get the fileKey
                    // 4. saveFile()
                    SecretKey deviceKey = ByteCrypto.str2Key(thisDevice.getKey());
                    secureFile.setFileKey(ByteCrypto.byte2key(ByteCrypto.decryptByte(secureFile.getCipherKey(), deviceKey)));
                    saveFile();
                } else {
                    // Create QR Code to send file header with a button
                    Intent intent = new Intent(this, SecureFileHeaderQRCodeActivity.class);
                    intent.putExtra(SecureFileHeaderQRCodeActivity.ACTION_INTENT_EXTRA, "GET_FILE_HEADER");
                    intent.putExtra(SecureFileHeaderQRCodeActivity.FILE_ID_INTENT_EXTRA, secureFile.getFileId());
                    intent.putExtra(SecureFileHeaderQRCodeActivity.FILE_CIPHER_KEY_INTENT_EXTRA, secureFile.getCipherKey());
                    startActivityForResult(intent, GET_FILE_HEADER);
                }
            }

        } catch (IOException e){
            Log.e(TAG, "File IO Exception");
        }
    }
}
