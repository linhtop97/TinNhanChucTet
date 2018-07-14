package com.vdt.trieuvu310198.goldtime.customtimezone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.data.DataTimeZone;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ListCountryActivity extends AppCompatActivity {
    private ListView lvNameCountry;
    private ArrayAdapter adapter;
    private List<String> listNameCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_country);
        inIt();
    }

    private void inIt() {
        dataNameCountry();
        lvNameCountry = findViewById(R.id.lv_nameCountry);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listNameCountry);
        lvNameCountry.setAdapter(adapter);
        lvNameCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataTimeZone dataTimeZone = new DataTimeZone();
                dataTimeZone.addIDAlarm(ListCountryActivity.this, new Integer(i));
                finish();
            }
        });
    }


    private void dataNameCountry() {
        listNameCountry = new ArrayList<>();
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            listNameCountry.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
    }
    private static String displayTimeZone(TimeZone tz) {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);
        String result = "";
        if (hours > 0) {
            result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
        } else {
            result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
        }
        return result;
    }
}
