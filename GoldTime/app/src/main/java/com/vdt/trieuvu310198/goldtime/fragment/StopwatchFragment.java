package com.vdt.trieuvu310198.goldtime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.StopwatachAdapter;
import com.vdt.trieuvu310198.goldtime.model.StopwatchModel;

import java.util.ArrayList;
import java.util.List;

public class StopwatchFragment extends Fragment {

    private TextView tvStopWhatch;

    private ToggleButton btStartStop;
    private Button btResetLap;

    private RecyclerView recyclerViewStopwatch;
    private StopwatachAdapter adapter;
    private Handler handler;

    private List<StopwatchModel> listStopwatch;

    private int seconds;
    private int milliSeconds;
    private int minute;
    private int hour;
    private int count = 0;

    private long millisecondTime = 0L;
    private long startTime = 0L;
    private long timeBuff = 0L;
    private long updateTime = 0L;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bam_gio, container, false);
        inIt(view);
        xuLi();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void inIt(View view) {
        tvStopWhatch = view.findViewById(R.id.tv_stop_watch);
        btStartStop = view.findViewById(R.id.bt_start_stop);
        btResetLap = view.findViewById(R.id.bt_reset_lap);
        recyclerViewStopwatch = view.findViewById(R.id.rv_stopwatch);
        btResetLap.setEnabled(false);


        btStartStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    timeBuff += millisecondTime;
                    handler.removeCallbacks(runnable);
                    btResetLap.setText("Reset");
                    btResetLap.setEnabled(true);
                } else {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    btResetLap.setText("Lap");
                    btResetLap.setEnabled(true);
                }
            }
        });


        btResetLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btResetLap.getText().equals("Reset")) {//reset
                    count = 0;
                    millisecondTime = 0L;
                    startTime = 0L;
                    timeBuff = 0L;
                    updateTime = 0L;
                    seconds = 0;
                    minute = 0;
                    milliSeconds = 0;

                    tvStopWhatch.setText("00:00:00");

                    listStopwatch.clear();

                    adapter.notifyDataSetChanged();
                    btResetLap.setEnabled(false);
                } else {//lap
                    listStopwatch.add(new StopwatchModel(count++, tvStopWhatch.getText().toString()));

                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void xuLi() {
        handler = new Handler();
        listStopwatch = new ArrayList<>();
        adapter = new StopwatachAdapter(listStopwatch, context);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayout.VERTICAL, true);
        recyclerViewStopwatch.setLayoutManager(manager);
        recyclerViewStopwatch.setAdapter(adapter);
    }


    public Runnable runnable = new Runnable() {

        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + millisecondTime;

            seconds = (int) (updateTime / 1000);

            minute = seconds / 60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 1000);

            tvStopWhatch.setText(String.format("%02d", minute) + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%02d", milliSeconds / 10));


            handler.postDelayed(this, 0);
        }

    };
}
