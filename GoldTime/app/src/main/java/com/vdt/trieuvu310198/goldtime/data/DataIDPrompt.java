package com.vdt.trieuvu310198.goldtime.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.List;

public class DataIDPrompt {
    public DataIDPrompt() {
        super();
    }

    public void saveDataIDPrompt(Context context, List<Integer> listIDPrompt) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORAGE_PROMPT, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonIDPrompt = gson.toJson(listIDPrompt);
        Log.e("JSON_DATA_SAVE", jsonIDPrompt);
        editor.putString(Constrain.LIST_DATA_ID_PROMPT, jsonIDPrompt);
        editor.commit();
    }

    public List<Integer> loadDataIDPrompt(Context context) {
        SharedPreferences settings;
        List<Integer> listID;
        settings = context.getSharedPreferences(Constrain.NAME_STORAGE_PROMPT, Context.MODE_PRIVATE);
        String jsonIDPrompt = settings.getString(Constrain.LIST_DATA_ID_PROMPT, null);
        Gson gson = new Gson();
        listID = gson.fromJson(jsonIDPrompt, new TypeToken<List<Integer>>() {
        }.getType());
        return listID;
    }

    public void addIDPrompt(Context context, Integer integer) {
        List<Integer> listIDPrompt = loadDataIDPrompt(context);
        if (listIDPrompt == null)
            listIDPrompt = new ArrayList();
        listIDPrompt.add(integer);
        saveDataIDPrompt(context, listIDPrompt);
    }

    public void removeDataIDPrompt(Context context, int position) {
        List<Integer> listIDPrompt = loadDataIDPrompt(context);
        Log.e("DATA_SIZE", listIDPrompt.size()+"");
        if (listIDPrompt != null) {
            listIDPrompt.remove(position);
            saveDataIDPrompt(context, listIDPrompt);
        }
        Log.e("DATA_SIZE", listIDPrompt.size()+"");
    }

    public void clearDataIDPrompt(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORAGE_PROMPT, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(Constrain.LIST_DATA_ID_PROMPT).commit();
//        List<Integer> listIDAlarm = loadDataIDAlarm(context);
//        if (listIDAlarm != null) {
//            listIDAlarm.clear();
//            saveDataIDAlarm(context, listIDAlarm);
//        }
    }
}
