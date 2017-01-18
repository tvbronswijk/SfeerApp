package com.hueandme.sfeer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

import java.util.Map;

public class SfeerConfigurationController {

    private SharedPreferences mSharedPreferences;

    private static final Object mLock = new Object();

    public SfeerConfigurationController(Context context) {
        mSharedPreferences = context.getSharedPreferences("config", 0);
    }

    public Map<String, SfeerConfiguration> getAll() {
        synchronized (mLock) {
            return new Gson().fromJson(mSharedPreferences.getString("setting", "{}"), new TypeToken<Map<String, SfeerConfiguration>>() {
            }.getType());
        }
    }

    public SfeerConfiguration get(String name) {
        return getAll().get(name);
    }

    public void save(SfeerConfiguration sfeerConfiguration) {
        synchronized (mLock) {
            Map<String, SfeerConfiguration> all = getAll();
            all.put(sfeerConfiguration.getName(), sfeerConfiguration);

            mSharedPreferences.edit().putString("setting", new Gson().toJson(all)).apply();

            Log.d("SfeerConfig", "Saved: " + new Gson().toJson(all));
        }
    }

    public void remove(SfeerConfiguration sfeerConfiguration) {
        synchronized (mLock) {
            Map<String, SfeerConfiguration> all = getAll();
            all.remove(sfeerConfiguration.getName());

            mSharedPreferences.edit().putString("setting", new Gson().toJson(all)).apply();

            Log.d("SfeerConfig", "Saved: " + new Gson().toJson(all));
        }
    }
}
