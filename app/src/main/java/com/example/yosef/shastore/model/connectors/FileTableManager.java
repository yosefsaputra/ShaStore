package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.components.EncryptedFile;

import javax.crypto.SecretKey;

public class FileTableManager {
    public SecretKey getFileKey(String fileId){

        EncryptedFile prev = AppDatabase.getDatabase().getEncFile(fileId);
        if (prev == null) return null;
        else return prev.getFileKey();
    }

    public boolean saveFile(EncryptedFile file){
        return AppDatabase.getDatabase().addEncFile(file);
    }
}
