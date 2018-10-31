package com.example.yosef.shastore.model;

import android.media.Image;

import com.example.yosef.shastore.model.components.QRCode;

public interface ShastoreInterface {
    boolean authenticate(String username, String password);

    QRCode readQRCode(Image image);
}
