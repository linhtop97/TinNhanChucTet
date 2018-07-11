package com.vdt.trieuvu310198.goldtime.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.TimeZoneAdater;
import com.vdt.trieuvu310198.goldtime.customtimezone.ListCountryActivity;
import com.vdt.trieuvu310198.goldtime.data.DataTimeZone;
import com.vdt.trieuvu310198.goldtime.model.RVTimeZoneModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

public class TimeZoneFragment extends Fragment implements View.OnClickListener {

    private List<RVTimeZoneModel> listTimeZone;// list của recycler view
    private List<String> listNameCountry; // list tên thanh phố
    private List<Integer> listPositionTimeZone;// list vị trí thành phố đã chọn để xem

    private RecyclerView recyclerView;
    private TimeZoneAdater adapter;
    private Context context;
    private FloatingActionButton fabAdd;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EEE", "oncreate");
        View view = inflater.inflate(R.layout.fragment_timeinterntional, container, false);
        inIt(view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        xuLi();
    }

    private void inIt(View view) {
        recyclerView = view.findViewById(R.id.rv_internaltionalTime);
        fabAdd = view.findViewById(R.id.btn_fab_add);
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fab_add:
                startActivity(new Intent(context, ListCountryActivity.class));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //    private void saveData() {
//        listInternaltional = new ArrayList<>();
//        data();
//        DataInternaltionalTime dataInternaltionalTime = new DataInternaltionalTime();
//        if (dataInternaltionalTime.loadDataInternaltionalTime(this) == null) {
//            dataInternaltionalTime.saveDataInterNaltionalTime(this, listInternaltional);
//        }
//    }

    private void xuLi() {
        listTimeZone = new ArrayList<>();
        readData();
        TimeZoneAdater.OnCLickListener onCLickListener = new TimeZoneAdater.OnCLickListener() {
            @Override
            public void onClick(View view, int position) {
                listTimeZone.remove(position);
                DataTimeZone dataTimeZone = new DataTimeZone();
                dataTimeZone.removeDataIDAlarm(context, position);
                adapter.notifyDataSetChanged();
            }
        };
        TimeZoneAdater.OnItemClickListener onItemClickListener = new TimeZoneAdater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        };
        adapter = new TimeZoneAdater(listTimeZone, context, onCLickListener, onItemClickListener);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void readData() {
        dataNameCountry();
        DataTimeZone dataTimeZone = new DataTimeZone();
        if (dataTimeZone.loadDataIDAlarm(context) != null) {
            listPositionTimeZone = dataTimeZone.loadDataIDAlarm(context);
        }
        String[] ids = TimeZone.getAvailableIDs();
        if (listPositionTimeZone != null) {
            for(int i = 0; i < listPositionTimeZone.size(); i++) {
                TimeZone tz = TimeZone.getTimeZone(ids[listPositionTimeZone.get(i).intValue()]);
                String nameCountry = tz.getID();
                long miliCurentSeconds = System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(tz.getRawOffset());
                Log.e("MILL", "" + TimeUnit.MILLISECONDS.toMillis(tz.getRawOffset()));
                long curentHour = (miliCurentSeconds%86400000)/3600000;
                Log.e("CURENTHOUR", TimeUnit.MILLISECONDS.toMillis(tz.getRawOffset()) + "");
                long curentMinute = (miliCurentSeconds%3600000)/60000;
                String time = String.format("%02d", curentHour) + ":" + String.format("%02d", curentMinute);
                String day;
                listTimeZone.add(new RVTimeZoneModel(nameCountry, "", time, R.drawable.ic_trash));

            }
        }
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

