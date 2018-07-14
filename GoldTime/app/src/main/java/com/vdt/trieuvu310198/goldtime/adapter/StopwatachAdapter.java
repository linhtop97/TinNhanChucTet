package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.StopwatchModel;

import java.util.List;

public class StopwatachAdapter extends RecyclerView.Adapter<StopwatachAdapter.ViewHolder> {

    private List<StopwatchModel> listStopwatch;
    private Context context;


    public StopwatachAdapter(List<StopwatchModel> listStopwatch, Context context) {
        this.listStopwatch = listStopwatch;
        this.context = context;
    }

    @NonNull
    @Override
    public StopwatachAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_stopswatch, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StopwatachAdapter.ViewHolder holder, int position) {
        StopwatchModel stopwatchModel = listStopwatch.get(position);
        holder.tvCount.setText(String.valueOf(stopwatchModel.getCount()));
        holder.tvTime.setText(stopwatchModel.getTime());
    }

    @Override
    public int getItemCount() {
        return listStopwatch.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCount;
        public TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
