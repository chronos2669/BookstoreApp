package com.example.bookstoreapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bookstoreapp.models.User;

public class SessionManager {
    private static final String PREF_NAME = "BookstoreSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getUserDetails() {
        if (!isLoggedIn()) return null;

        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, -1));
        user.setUsername(pref.getString(KEY_USERNAME, null));
        user.setRole(pref.getString(KEY_ROLE, null));
        user.setEmail(pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isAdmin() {
        return "admin".equals(pref.getString(KEY_ROLE, ""));
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }
}