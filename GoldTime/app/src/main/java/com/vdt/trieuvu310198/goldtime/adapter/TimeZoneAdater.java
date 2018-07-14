package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.RVTimeZoneModel;

import java.util.List;

public class TimeZoneAdater extends RecyclerView.Adapter<TimeZoneAdater.ViewHolder> {

    private List<RVTimeZoneModel> liInternaltional;
    private Context context;
    private OnCLickListener mOnclick;
    private OnItemClickListener mOnItemClick;

    public TimeZoneAdater(List<RVTimeZoneModel> liInternaltional, Context context,
                          OnCLickListener onCLickListener, OnItemClickListener onItemClickListener) {
        this.liInternaltional = liInternaltional;
        this.context = context;
        this.mOnclick = onCLickListener;
        this.mOnItemClick = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_international_time, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        RVTimeZoneModel rvInternaltionnalModel = liInternaltional.get(position);
        holder.tvNameCountry.setText(rvInternaltionnalModel.getNameCountry());
        holder.tvDay.setText(rvInternaltionnalModel.getDay());
        holder.tvTime.setText(rvInternaltionnalModel.getTime());
        holder.icPopup.setImageResource(rvInternaltionnalModel.getIcpopupmenu());

        holder.icPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclick.onClick(view, position);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mOnItemClick.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liInternaltional.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCountry;
        public TextView tvDay;
        public TextView tvTime;
        public ImageView icPopup;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNameCountry = itemView.findViewById(R.id.tv_nameCountry);
            tvDay = itemView.findViewById(R.id.tv_day_timeInternaltional);
            tvTime = itemView.findViewById(R.id.tv_timeIternaltional);
            icPopup = itemView.findViewById(R.id.ic_popup_menu);
            view = itemView;
        }
    }
    public interface OnCLickListener {
        public void onClick(View view, int position);
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
