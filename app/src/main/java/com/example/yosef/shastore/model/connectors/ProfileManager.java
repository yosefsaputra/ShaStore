package com.example.yosef.shastore.model.connectors;

import com.example.yosef.shastore.database.AppDatabase;
import com.example.yosef.shastore.model.components.Profile;

public class ProfileManager {
    public boolean authenticateProfile(String username, String password) {
        Profile profile = AppDatabase.getDatabase().getProfile(username);

        if (profile != null) {
            HashMachine hm = new GenericHashMachine();
            hm.setText(password);
            ((GenericHashMachine) hm).setSalt(profile.getPasswordSalt());
            hm.execute();

            String passwordHash = hm.getHashedText();

            if (passwordHash == null) {
                return false;
            } else {
                return passwordHash.equals(profile.getPasswordHash());
            }
        } else {
            return false;
        }
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

        return AppDatabase.getDatabase().addProfile(newProfile);
    }
}
