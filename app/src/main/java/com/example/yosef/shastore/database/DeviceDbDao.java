package com.example.yosef.shastore.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

@Dao
public interface DeviceDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DeviceDb deviceDb);

    @Update
    void update(DeviceDb deviceDb);

    @Delete
    void delete(DeviceDb deviceDb);
}
