package com.example.linhnb.project1zergitas.screen.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class ListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mData;

    protected OnItemClickListener mItemClickListener;
    protected OnItemLongClickListener mItemLongClickListener;

    public ListAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void clear() {
        if (mData != null)
            mData.clear();
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public OnItemLongClickListener getItemLongClickListener() {
        return mItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }
}
