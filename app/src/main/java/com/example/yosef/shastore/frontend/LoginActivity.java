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

package com.example.yosef.shastore.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yosef.shastore.R;
import com.example.yosef.shastore.model.connectors.ProfileManager;
import com.example.yosef.shastore.model.util.SharedPreferenceHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = SharedPreferenceHandler.getSharedPrefsCurrentUserSettings(this);

        if (sharedPrefs.getString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, null) != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_login);

            // Set up the login form.
            mUsernameView = findViewById(R.id.username);

            mPasswordView = findViewById(R.id.password);

            Button mSignInButton = findViewById(R.id.sign_in_button);
            mSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            Button mRegisterButton = findViewById(R.id.register_button);
            mRegisterButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerProfile();
                }
            });
        }
    }

    private void registerProfile() {
        String registerUsername = mUsernameView.getText().toString();

        ProfileManager profileManager = new ProfileManager();
        if (profileManager.registerProfile(registerUsername, mPasswordView.getText().toString())) {
            Toast.makeText(this, String.format("Registration successful %s", mUsernameView.getText().toString()), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, String.format("Registration unsuccessful %s", mUsernameView.getText().toString()), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String attemptUsername = mUsernameView.getText().toString();

        ProfileManager profileManager = new ProfileManager();
        if (profileManager.authenticateProfile(attemptUsername, mPasswordView.getText().toString())) {
            sharedPrefs.edit().putString(SharedPreferenceHandler.SHARED_PREFS_CURRENT_PROFILE_USERNAME, attemptUsername).apply();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, String.format("Authentication unsuccessful %s", mUsernameView.getText().toString()), Toast.LENGTH_LONG).show();
        }
    }
}

