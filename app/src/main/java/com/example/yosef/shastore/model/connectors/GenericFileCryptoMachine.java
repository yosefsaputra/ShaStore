package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.components.RegularFile;
import com.example.yosef.shastore.model.components.SecureFile;

public class GenericFileCryptoMachine extends FileCryptoMachine {
    @Override
    public EncryptedFile encryptFile(RegularFile regularFile, byte[] fileKey) {
        return null;
    }

    @Override
    public SecureFile secureFile(EncryptedFile encryptedFile, byte[] deviceKey) {
        return null;
    }

    @Override
    public EncryptedFile unsecureFile(SecureFile secureFile, byte[] deviceKey) {
        return null;
    }

    @Override
    public RegularFile decryptFile(EncryptedFile encryptedFile, byte[] fileKey) {
        return null;
    }
}
