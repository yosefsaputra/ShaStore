package com.example.yosef.shastore.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

@Dao
public interface ProfileDbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProfileDb profileDb);

    @Update
    void update(ProfileDb profileDb);

    @Delete
    void delete(ProfileDb profileDb);
}
