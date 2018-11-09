package com.example.yosef.shastore.database.converters;

import com.example.yosef.shastore.database.ProfileDb;
import com.example.yosef.shastore.model.components.Profile;

public class ProfileConverter {
    public static Profile toProfile(ProfileDb profileDb) {
        if (profileDb == null) {
            return null;
        }

        Profile profile = new Profile();

        profile.setUsername(profileDb.getUsername());
        profile.setPasswordHash(profileDb.getPasswordHash());
        profile.setPasswordSalt(profileDb.getPasswordSalt());

        return profile;
    }

    public static ProfileDb toProfileDb(Profile profile) {
        if (profile == null) {
            return null;
        }

        ProfileDb profileDb = new ProfileDb();

        profileDb.setUsername(profile.getUsername());
        profileDb.setPasswordHash(profile.getPasswordHash());
        profileDb.setPasswordSalt(profile.getPasswordSalt());

        return profileDb;
    }
}
