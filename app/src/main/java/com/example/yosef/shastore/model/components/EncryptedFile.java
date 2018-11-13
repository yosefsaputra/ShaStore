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

package com.example.yosef.shastore.model.components;

import android.util.Log;

import com.example.yosef.shastore.model.connectors.ByteCrypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class EncryptedFile extends FileObject {

    private String fileId;
    private SecretKey fileKey;
    private byte[] cipherKey;
    public static int headerLength = 82;
    public static int fileIdLength = 34;
    public static int cipherKeyLength = 48;
    public static String generateFileId(String deviceId){
        long mills = System.currentTimeMillis();
        String secs = Long.toString(mills);
        String fileid = deviceId + "T" + secs;
        //To use the fileId, make the fileId to a Base64 String!!!
        fileid = ByteCrypto.byte2Str(ByteCrypto.str2Byte(fileid));
        return fileid;
    }
    public void setFileId(String fileId){
        this.fileId = fileId;
    }
    public void setFileKey(SecretKey fileKey){
        this.fileKey = fileKey;
    }
    public String getFileId() {return fileId;}
    public SecretKey getFileKey() {return fileKey;}
    public void setCipherKey(byte[] cipherKey) {this.cipherKey = cipherKey;}


    public EncryptedFile(){
        name = "";
        content = null;
    }


    public EncryptedFile(String name)
    {
        this.name = name;
        content = null;
    }

    @Override
    public void writeContent(OutputStream outputStream) throws IOException {
        //Log.d(TAG, "writing to file content:" + new String(content));

        outputStream.write(ByteCrypto.str2Byte(fileId));
        outputStream.write(cipherKey);
        //headerLength = ByteCrypto.str2Byte(fileId).length + cipherKey.length;
//        Log.i("!!!!!!!!", fileId+"!!!");
//        Log.i("!!!!!!!!", Arrays.toString(ByteCrypto.str2Byte(fileId)));
//        Log.i("!!!!!!!!", ByteCrypto.byte2Str(ByteCrypto.str2Byte(fileId))+"!!!");
//
//        Log.i("!!!!!!!!", Arrays.toString(fileKey.getEncoded()));
//        Log.i("!!!!!!!!", Arrays.toString(ByteCrypto.str2Key(ByteCrypto.key2Str(fileKey)).getEncoded()));
//        Log.i("!!!!!!!!", ByteCrypto.str2Byte(fileId).length+"");
//        Log.i("!!!!!!!!", cipherKey.length+"");
        outputStream.write(content);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void readContent(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        // this is storage overwritten on each iteration with bytes
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        byte[] fileContent = byteBuffer.toByteArray();
        fileId = ByteCrypto.byte2Str(Arrays.copyOfRange(fileContent, 0, fileIdLength));
        cipherKey = Arrays.copyOfRange(fileContent, fileIdLength, headerLength);
        content = Arrays.copyOfRange(fileContent, headerLength, fileContent.length);
    }

}
