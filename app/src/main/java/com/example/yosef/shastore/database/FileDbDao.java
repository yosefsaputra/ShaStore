package com.example.yosef.shastore.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FileDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FileDb fileDb);

    @Update
    void update(FileDb fileDb);

    @Delete
    void delete(FileDb fileDb);

    @Query("SELECT * FROM filedb WHERE fileId=:fileId")
    FileDb getFileDb(String fileId);

    @Query("SELECT * FROM filedb")
    List<FileDb> getAllFileDbs();

    @Query("DELETE FROM filedb")
    void deleteAllFileDbs();
}
