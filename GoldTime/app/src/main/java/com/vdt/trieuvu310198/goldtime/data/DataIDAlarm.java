package com.vdt.trieuvu310198.goldtime.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.List;

public class DataIDAlarm {
    public DataIDAlarm() {
        super();
    }

    public void saveDataIDAlarm(Context context, List<Integer> listIDAlarm) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORGE_ALARM, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonIDAlarm = gson.toJson(listIDAlarm);
        Log.e("JSON_DATA_SAVE", jsonIDAlarm);
        editor.putString(Constrain.LIST_DATA_ID_ALARM, jsonIDAlarm);
        editor.commit();
    }

    public List<Integer> loadDataIDAlarm(Context context) {
        SharedPreferences settings;
        List<Integer> listID;
        settings = context.getSharedPreferences(Constrain.NAME_STORGE_ALARM, Context.MODE_PRIVATE);
        String jsonAlarm = settings.getString(Constrain.LIST_DATA_ID_ALARM, null);
        Gson gson = new Gson();
        listID = gson.fromJson(jsonAlarm, new TypeToken<List<Integer>>() {
        }.getType());
        return listID;
    }

    public void addIDAlarm(Context context, Integer integer) {
        List<Integer> listIDAlarm = loadDataIDAlarm(context);
        if (listIDAlarm == null)
            listIDAlarm = new ArrayList();
        listIDAlarm.add(integer);
        saveDataIDAlarm(context, listIDAlarm);
    }

    public void removeDataIDAlarm(Context context, int position) {
        List<Integer> listIDAlarm = loadDataIDAlarm(context);
        Log.e("DATA_SIZE", listIDAlarm.size()+"");
        if (listIDAlarm != null) {
            listIDAlarm.remove(position);
            saveDataIDAlarm(context, listIDAlarm);
        }
        Log.e("DATA_SIZE", listIDAlarm.size()+"");
    }

    public void clearDataIDAlarm(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORGE_ALARM, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(Constrain.LIST_DATA_ID_ALARM).commit();
//        List<Integer> listIDAlarm = loadDataIDAlarm(context);
//        if (listIDAlarm != null) {
//            listIDAlarm.clear();
//            saveDataIDAlarm(context, listIDAlarm);
//        }
    }
}
