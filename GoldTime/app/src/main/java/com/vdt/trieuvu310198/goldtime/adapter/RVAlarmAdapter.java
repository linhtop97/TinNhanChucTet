package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.AlarmRV;

import java.util.List;

public class RVAlarmAdapter extends RecyclerView.Adapter<RVAlarmAdapter.ViewHolder> {

    private List<AlarmRV> listAlarmRV;
    private Context context;
    private OnItemClickListener mListener;
    private OnCheckedChangedListener mChecked;
    private OnCLickListener mOnclick;

    public RVAlarmAdapter(List<AlarmRV> listAlarmRV, Context context, OnItemClickListener listener,
                          OnCheckedChangedListener Checked, OnCLickListener onClick) {
        this.mListener = listener;
        this.listAlarmRV = listAlarmRV;
        this.context = context;
        this.mChecked = Checked;
        this.mOnclick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_rvalarm_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        AlarmRV alarmRV = listAlarmRV.get(position);
        holder.txtHour.setText(alarmRV.getHour());
        holder.txtMinute.setText(alarmRV.getMinute());
        holder.txtDay.setText(alarmRV.getDay());
        holder.tbBell.setChecked(alarmRV.getAlarmStatus());
        holder.icPopupMenu.setImageResource(alarmRV.getPopupMenu());
        holder.tbBell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mChecked.onChecked(compoundButton, b, position);
            }
        });
        holder.icPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclick.onClick(view, position);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return listAlarmRV.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHour;
        public TextView txtMinute;
        public TextView txtDay;
        public ToggleButton tbBell;
        public ImageView icPopupMenu;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            txtHour = itemView.findViewById(R.id.txt_hour);
            txtMinute = itemView.findViewById(R.id.txt_minute);
            txtDay = itemView.findViewById(R.id.txt_day_alarm);
            tbBell = itemView.findViewById(R.id.tb_bell_alarm);
            icPopupMenu = itemView.findViewById(R.id.ic_popup_menu);
            view = itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnCheckedChangedListener {
        public void onChecked(CompoundButton compoundButton, boolean b, int position);
    }

    public interface OnCLickListener {
        public void onClick(View view, int position);
    }




}
