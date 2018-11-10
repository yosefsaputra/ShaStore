package com.example.yosef.shastore.model.connectors;

import android.util.Log;


import java.lang.reflect.Array;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ByteCrypto {
    private  static byte[] IV;
    public static String key2Str(SecretKey key){
        try{
            return new String(key.getEncoded(), "UTF-8");
        } catch (Exception e){
            return "";
        }

    }

    public static SecretKey str2Key(String str){
        try{
            byte[] strB = str.getBytes("UTF-8");
            return new SecretKeySpec(strB, 0, strB.length, "AES");
        }catch (Exception e){
            return null;
        }
    }

    public static String generateRandKey(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey newK = keyGen.generateKey();
            byte[] arr = new byte[]{118, -37, 38, 74, -70, -58, -14, -110, 94, 118, -5, -112, 60, -4, -76, 125};
//            Log.i("!!!!!!!", Arrays.toString(newK.getEncoded()));
//            Log.i("!!!!!!!", key2Str(newK).length() + "");
//            Log.i("!!!!!!!", Arrays.toString(key2Str(newK).getBytes("UTF-8")));
            String newS = new String(arr, "UTF-8");
            Log.i("!!!!!!!!", newS);
            Log.i("!!!!!!!!",Arrays.toString(newS.getBytes("UTF-8")));
            return ByteCrypto.key2Str(newK);
        } catch (Exception e){
            Log.e("In Device", e.toString());
            return "";
        }
    }

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
            return cipher.doFinal(cipherText);
        } catch (Exception e){
            Log.e("!!!!!!", "In decryptByte: " + e.toString());
            return null;
        }
    }
}
