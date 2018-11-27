package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.screen.base.adapter.IAdapterView;
import com.example.linhnb.project1zergitas.screen.base.adapter.ListAdapter;
import com.example.linhnb.project1zergitas.screen.base.adapter.OnItemClickListener;

import java.util.List;

public class WallpaperAdapter extends ListAdapter<String> {


    public WallpaperAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WallpaperViewHolder wallpaperViewHolder = (WallpaperViewHolder) holder;
        wallpaperViewHolder.bind(mData.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ringtone, parent, false);
        return new WallpaperViewHolder(view, mItemClickListener);
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder implements IAdapterView<String> {

        private TextView mName;
        private RadioButton mRadioSelected;
        private OnItemClickListener mOnItemClickListener;

        public WallpaperViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mOnItemClickListener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public void bind(String item) {
            if (item == null) {
                return;
            }
        }
    }
}
