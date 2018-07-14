package com.vdt.trieuvu310198.goldtime.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdt.trieuvu310198.goldtime.model.PromptModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.List;

public class DataPrompt {
    public DataPrompt() {
        super();
    }

    public void saveDataPrompt(Context context, List<PromptModel> listPrompt) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constrain.NAME_STORAGE_PROMPT, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonPrompt = gson.toJson(listPrompt);
        //Log.e("JSON_DATA_SAVE", jsonPrompt);
        editor.putString(Constrain.LIST_DATA_PROMPT, jsonPrompt);
        editor.commit();
    }

    public List<PromptModel> loadDataPrompt(Context context) {
        SharedPreferences settings;
        List<PromptModel> listPrompt;
        settings = context.getSharedPreferences(Constrain.NAME_STORAGE_PROMPT, Context.MODE_PRIVATE);
        String jsonPrompt = settings.getString(Constrain.LIST_DATA_PROMPT, null);
        Gson gson = new Gson();
        listPrompt = gson.fromJson(jsonPrompt, new TypeToken<List<PromptModel>>() {
        }.getType());
        return listPrompt;
    }

    public void addPrompt(Context context, PromptModel promptModel) {
        List<PromptModel> listPrompt = loadDataPrompt(context);
        if (listPrompt == null)
            listPrompt = new ArrayList();
        listPrompt.add(promptModel);
        saveDataPrompt(context, listPrompt);
    }

    public void removeDataPrompt(Context context, int position) {
        List<PromptModel> listPrompt = loadDataPrompt(context);
        //Log.e("DATA_SIZE", listPrompt.size()+"");
        if (listPrompt != null) {
            listPrompt.remove(position);
            saveDataPrompt(context, listPrompt);
        }
        //Log.e("DATA_SIZE", listAlarm.size()+"");
    }

}
