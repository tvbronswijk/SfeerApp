package com.hueandme.hue;

import android.content.Context;
import android.content.SharedPreferences;

public class HueSharedPreferences {

    private static final String HUE_SHARED_PREFERENCES_STORE = "HueSharedPrefs";
    private static final String LAST_CONNECTED_USERNAME = "lastConnectedUsername";
    private static final String LAST_CONNECTED_IP = "lastConnectedIP";
    private static HueSharedPreferences INSTANCE;
    private SharedPreferences mSharedPreferences;

    public static synchronized HueSharedPreferences getInstance(Context ctx) {
            if (INSTANCE == null) {
                INSTANCE = new HueSharedPreferences(ctx);
            }

        return INSTANCE;
    }

    private HueSharedPreferences(Context appContext) {
        mSharedPreferences = appContext.getSharedPreferences(HUE_SHARED_PREFERENCES_STORE, 0); // 0 - for private mode
    }


    public String getUsername() {
        return mSharedPreferences.getString(LAST_CONNECTED_USERNAME, "");
    }

    public boolean setUsername(String username) {
        return mSharedPreferences
                .edit()
                .putString(LAST_CONNECTED_USERNAME, username)
                .commit();
    }

    public String getLastConnectedIPAddress() {
        return mSharedPreferences.getString(LAST_CONNECTED_IP, "");
    }

    public boolean setLastConnectedIPAddress(String ipAddress) {
        return mSharedPreferences
                .edit()
                .putString(LAST_CONNECTED_IP, ipAddress)
                .commit();
    }
}
