package com.example.yosef.shastore.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FileDb {
    @PrimaryKey
    @NonNull
    private String fileId;
    private String fileKey;

    @NonNull
    public String getFileId() {
        return fileId;
    }

    public void setFileId(@NonNull String fileId) {
        this.fileId = fileId;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    @Override
    public String toString() {
        return "FileDb{" +
                "fileId='" + fileId + '\'' +
                ", fileKey='" + fileKey + '\'' +
                '}';
    }
}
