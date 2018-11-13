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

package com.example.yosef.shastore.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.yosef.shastore.database.converters.DeviceConverter;
import com.example.yosef.shastore.database.converters.EncryptedFileConverter;
import com.example.yosef.shastore.database.converters.ProfileConverter;
import com.example.yosef.shastore.model.components.Device;
import com.example.yosef.shastore.model.components.EncryptedFile;
import com.example.yosef.shastore.model.components.Profile;

import java.util.List;
import java.util.Locale;

@Database(entities = {ProfileDb.class, DeviceDb.class, FileDb.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static void initializeDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "AppDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    public static AppDatabase getDatabase() throws NullPointerException {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new NullPointerException("AppDatabase has not been initialized");
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract ProfileDbDao profileDbDao();

    public abstract DeviceDbDao deviceDbDao();

    public abstract FileDbDao fileDbDao();


    public Device getDevicebyId(String uuid){
        return DeviceConverter.toDevice(deviceDbDao().getDeviceById(uuid));
    }

    public boolean addDevice(Device newD){
        return deviceDbDao().insert(DeviceConverter.toDeviceDb(newD))!= 0;
    }

    public Profile getProfile(String username) {
        return ProfileConverter.toProfile(profileDbDao().getProfileDb(username));
    }

    public EncryptedFile getEncFile(String fileId){
        return EncryptedFileConverter.toEncFile(fileDbDao().getFileDb(fileId));
    }

    public boolean addEncFile(EncryptedFile file){
        return fileDbDao().insert(EncryptedFileConverter.toFileDb(file)) != 0;
    }
    public boolean addProfile(Profile profile) {
        return profileDbDao().insert(ProfileConverter.toProfileDb(profile)) != 0;
    }

    public FileDb getFileDb(String fileId) {
        return fileDbDao().getFileDb(fileId);
    }

    public void resetDatabase() {
        profileDbDao().deleteAllProfileDbs();
        deviceDbDao().deleteAllDeviceDbs();
        fileDbDao().deleteAllFileDbs();
    }

    public String toString() {
        return "===== Database =====\n" +
                toStringProfileDbs()
                + "\n" +
                toStringDeviceDbs()
                + "\n" +
                toStringFileDbs()
                + "\n" +
                "====================";
    }

    private String toStringProfileDbs() {
        StringBuilder stringBuilder = new StringBuilder();
        List<ProfileDb> profileDbs = getDatabase().profileDbDao().getAllProfileDbs();
        if (profileDbs == null) {
            stringBuilder.append("Number of ProfileDb : 0");
        } else {
            stringBuilder.append(String.format(Locale.getDefault(), "Number of ProfileDb : %d", profileDbs.size()));
            for (ProfileDb profileDb : profileDbs) {
                stringBuilder.append("\n");
                stringBuilder.append("- ");
                stringBuilder.append(profileDb.toString());
            }
        }

        return stringBuilder.toString();
    }

    private String toStringDeviceDbs() {
        StringBuilder stringBuilder = new StringBuilder();
        List<DeviceDb> deviceDbs = getDatabase().deviceDbDao().getAllDeviceDbs();
        if (deviceDbs == null) {
            stringBuilder.append("Number of DeviceDb : 0");
        } else {
            stringBuilder.append(String.format(Locale.getDefault(), "Number of DeviceDb : %d", deviceDbs.size()));
            for (DeviceDb deviceDb : deviceDbs) {
                stringBuilder.append("\n");
                stringBuilder.append("- ");
                stringBuilder.append(deviceDb.toString());
            }
        }

        return stringBuilder.toString();
    }

    private String toStringFileDbs() {
        StringBuilder stringBuilder = new StringBuilder();
        List<FileDb> fileDbs = getDatabase().fileDbDao().getAllFileDbs();
        if (fileDbs == null) {
            stringBuilder.append("Number of FileDb : 0");
        } else {
            stringBuilder.append(String.format(Locale.getDefault(), "Number of FileDb : %d", fileDbs.size()));
            for (FileDb fileDb : fileDbs) {
                stringBuilder.append("\n");
                stringBuilder.append("- ");
                stringBuilder.append(fileDb.toString());
            }
        }

        return stringBuilder.toString();
    }
}
