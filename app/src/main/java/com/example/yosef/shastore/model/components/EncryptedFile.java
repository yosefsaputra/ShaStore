package com.example.yosef.shastore.model.components;

public class EncryptedFile extends FileObject {
    protected SecureHeader header;
    protected byte[] securedContent;

    public class SecureHeader {
        protected byte[] fileKey;
    }
}
