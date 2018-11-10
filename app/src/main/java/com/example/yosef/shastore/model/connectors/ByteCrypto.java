package com.example.yosef.shastore.model.connectors;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class ByteCrypto {
    private  static byte[] IV;
    public static byte[] encryptByte(byte[] plainText, SecretKey fileKey){
        Cipher cipher;
        try{
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, fileKey);
            return cipher.doFinal(plainText);
        } catch (Exception e){
            Log.e("", "In encryptByte: " + e.toString());
            return null;
        }
    }

    public static byte[] decryptByte(byte[] cipherText, SecretKey fileKey){
        Cipher cipher;
        try{
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, fileKey);
            Log.i("!!!!!!","In dec3");
            return cipher.doFinal(cipherText);
        } catch (Exception e){
            Log.e("!!!!!!", "In decryptByte: " + e.toString());
            return null;
        }
    }
}
