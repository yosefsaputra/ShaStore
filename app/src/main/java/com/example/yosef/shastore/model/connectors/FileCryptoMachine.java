package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.model.components.SecureFile;
import com.example.yosef.shastore.model.components.RegularFile;
import com.example.yosef.shastore.model.components.EncryptedFile;

public abstract class FileCryptoMachine {
    public abstract EncryptedFile encryptFile(RegularFile regularFile, byte[] fileKey);
    public abstract SecureFile secureFile(EncryptedFile encryptedFile, byte[] deviceKey);

    public abstract EncryptedFile unsecureFile(SecureFile secureFile, byte[] deviceKey);
    public abstract RegularFile decryptFile(EncryptedFile encryptedFile, byte[] fileKey);
}
