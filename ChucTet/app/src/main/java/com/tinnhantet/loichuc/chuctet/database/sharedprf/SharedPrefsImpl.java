package com.tinnhantet.loichuc.chuctet.database.sharedprf;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tinnhantet.loichuc.chuctet.models.Message;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsImpl implements SharedPrefsApi {

    private static final String PREFS_NAME = "Codia";

    private SharedPreferences mSharedPreferences;

    public SharedPrefsImpl(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (clazz == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        }
        return null;
    }

    @Override
    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    @Override
    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public List<Message> getListMsg() {
        String tracks = mSharedPreferences.getString(SharedPrefsKey.KEY_LIST_MSG, null);
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        return new Gson().fromJson(tracks, listType);
    }

    public void putListMsg(List<Message> messages) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SharedPrefsKey.KEY_LIST_MSG, new Gson().toJson(messages)).apply();
    }
}
