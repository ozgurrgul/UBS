package dk.ozgur.ubs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CPM {

    private final SharedPreferences mPrefs;

    public CPM(final Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putBoolean(String name, boolean value) {
        mPrefs.edit().putBoolean(name, value).apply();
    }

    public void putInt(String name, int value) {
        mPrefs.edit().putInt(name, value).apply();
    }

    public void putString(String name, String value) {
        mPrefs.edit().putString(name, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPrefs.getBoolean(key, defaultValue);
    }

    public String getString(String key) {
        return mPrefs.getString(key, null);
    }

    public int getInt(String key) {
        return mPrefs.getInt(key, -1);
    }

}