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
