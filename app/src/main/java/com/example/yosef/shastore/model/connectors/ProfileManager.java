package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Profile;

public class ProfileManager {
    public boolean authenticateProfile(String username, String password) {
        // TODO: implement
        return false;
    }

    public boolean registerProfile(String username, String password) {
        String passwordSalt = "";
        String passwordHash;

        HashMachine hm = new GenericHashMachine();
        hm.setText(password);
        ((GenericHashMachine) hm).setSalt(passwordSalt);
        hm.execute();

        passwordHash = hm.getHashedText();

        Profile newProfile = new Profile();
        newProfile.setUsername(username);
        newProfile.setPasswordHash(passwordHash);
        newProfile.setPasswordSalt(passwordSalt);

        AppDatabase.getDatabase().addProfile(newProfile);
        return false;
    }
}
