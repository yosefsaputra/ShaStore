package com.example.yosef.shastore.model.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorageHandler {
    public static boolean checkFile(Context context, String filename) {
        try {
            context.openFileInput(filename);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static boolean createFile(Context context, String filename, byte[] contentBytes) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(contentBytes);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] readFile(Context context, String filename, byte[] contentBytes) {
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            fileInputStream.read(contentBytes);
            return contentBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
