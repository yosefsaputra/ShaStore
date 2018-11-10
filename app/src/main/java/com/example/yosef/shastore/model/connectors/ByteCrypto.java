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
