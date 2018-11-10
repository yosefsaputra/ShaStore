package com.example.yosef.shastore.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeviceDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DeviceDb deviceDb);

    @Update
    void update(DeviceDb deviceDb);

    @Delete
    void delete(DeviceDb deviceDb);

    @Query("SELECT * FROM devicedb")
    List<DeviceDb> getAllDeviceDbs();

    @Query("SELECT * FROM devicedb WHERE UUID=:uuid")
    DeviceDb getDeviceById(String uuid);

    @Query("DELETE FROM devicedb")
    void deleteAllDeviceDbs();
}
