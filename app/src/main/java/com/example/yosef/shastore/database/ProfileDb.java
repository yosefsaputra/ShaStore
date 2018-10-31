package com.example.yosef.shastore.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProfileDb {
    @PrimaryKey
    private String username;
    private String passwordHash;
}
