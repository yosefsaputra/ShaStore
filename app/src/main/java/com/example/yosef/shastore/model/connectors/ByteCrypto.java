package com.example.yosef.shastore.model.connectors;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class ByteCrypto {

    static byte[] encryptByte(byte[] plainText, SecretKey fileKey){
        Cipher cipher;
        try{
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, fileKey);
            return cipher.doFinal(plainText);
        } catch (Exception e){
            Log.e("", "In encryptByte: " + e.toString());
            return null;
        }
    }

    static byte[] decryptByte(byte[] cipherText, SecretKey fileKey){
        Cipher cipher;
        try{
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, fileKey);
            return cipher.doFinal(cipherText);
        } catch (Exception e){
            Log.e("", "In decryptByte: " + e.toString());
            return null;
        }
    }
}
