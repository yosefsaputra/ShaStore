package com.example.yosef.shastore.database.converters;

import com.example.yosef.shastore.database.FileDb;
import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.connectors.ByteCrypto;

public class EncryptedFileConverter {
    public static FileDb toFileDb(EncryptedFile file){
        FileDb newF =new  FileDb();
        newF.setFileId(file.getFileId());
        newF.setFileKey(ByteCrypto.key2Str(file.getFileKey()));
        return newF;
    }

    public static EncryptedFile toEncFile(FileDb file){
        EncryptedFile newF =new EncryptedFile();
        newF.setFileId(file.getFileId());
        newF.setFileKey(ByteCrypto.str2Key(file.getFileKey()));
        return newF;
    }
}
