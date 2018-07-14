package com.vdt.trieuvu310198.goldtime.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataAlarm {

    public DataAlarm() {
        super();
    }

    public void saveDataAlarm(Context context, List<DataAlarmModel> listAlarm) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORGE_ALARM, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonAlarm = gson.toJson(listAlarm);
        Log.e("JSON_DATA_SAVE", jsonAlarm);
        editor.putString(Constrain.LIST_DATA_ALARM, jsonAlarm);
        editor.commit();
    }

    public List<DataAlarmModel> loadDataAlarm(Context context) {
        SharedPreferences settings;
        List<DataAlarmModel> listAlarm;
        settings = context.getSharedPreferences(Constrain.NAME_STORGE_ALARM, Context.MODE_PRIVATE);
        String jsonAlarm = settings.getString(Constrain.LIST_DATA_ALARM, null);
        Gson gson = new Gson();
        listAlarm = gson.fromJson(jsonAlarm, new TypeToken<List<DataAlarmModel>>() {
        }.getType());
        return listAlarm;
    }

    public void addAlarm(Context context, DataAlarmModel dataAlarmModel) {
        List<DataAlarmModel> listAlarm = loadDataAlarm(context);
        if (listAlarm == null)
            listAlarm = new ArrayList();
        listAlarm.add(dataAlarmModel);
        saveDataAlarm(context, listAlarm);
    }

    public void removeDataAlarm(Context context, int position) {
        List<DataAlarmModel> listAlarm = loadDataAlarm(context);
        Log.e("DATA_SIZE", listAlarm.size()+"");
        if (listAlarm != null) {
            listAlarm.remove(position);
            saveDataAlarm(context, listAlarm);
        }
        Log.e("DATA_SIZE", listAlarm.size()+"");
    }

    public void sortDataAlarm(Context context) {
        List<DataAlarmModel> listAlarm = loadDataAlarm(context);
        if (listAlarm != null) {
            Collections.sort(listAlarm, new Comparator<DataAlarmModel>() {
                @Override
                public int compare(DataAlarmModel dataAlarmModel, DataAlarmModel t1) {
                    if (dataAlarmModel.getHour() > t1.getHour()) {
                        return 1;
                    }
                    else if (dataAlarmModel.getHour() == t1.getHour()
                            && dataAlarmModel.getMinute() > t1.getMinute()) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });
        }
        saveDataAlarm(context, listAlarm);
    }

}

