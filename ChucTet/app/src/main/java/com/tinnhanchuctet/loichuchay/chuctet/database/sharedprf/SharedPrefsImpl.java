package com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsImpl implements SharedPrefsApi {

    private static final String PREFS_NAME = "Codia";

    private SharedPreferences mPreferences;

    public SharedPrefsImpl(Context context) {
        this.mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) mPreferences.getString(key, "");
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(mPreferences.getBoolean(key, false));
        } else if (clazz == Float.class) {
            return (T) Float.valueOf(mPreferences.getFloat(key, 0));
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(mPreferences.getInt(key, 0));
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(mPreferences.getLong(key, 0));
        }
        return null;
    }

    @Override
    public <T> void put(String key, T data) {
        SharedPreferences.Editor edit = mPreferences.edit();
        if (data instanceof String) {
            edit.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            edit.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            edit.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            edit.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            edit.putLong(key, (Long) data);
        }
        edit.apply();
    }

    @Override
    public void clear() {
        mPreferences.edit().clear().apply();
    }

    public List<Message> getListMsg() {
        String tracks = mPreferences.getString(SharedPrefsKey.KEY_LIST_MSG, null);
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        return new Gson().fromJson(tracks, listType);
    }

    public void putListMsg(List<Message> messages) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SharedPrefsKey.KEY_LIST_MSG, new Gson().toJson(messages)).apply();
    }
}
