package com.ledbanner.ledmobile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ledbanner.ledmobile.R;
import com.ledbanner.ledmobile.listeners.OnItemClickListener;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Integer> mData;


    private OnItemClickListener mListener;

    public ColorAdapter(Context context, List<Integer> data) {
        mContext = context;
        mData = data;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
        colorViewHolder.bind(mData.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view, mListener);
    }


    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        private ImageButton colorButton;
        private OnItemClickListener mOnItemClickListener;

        public ColorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            colorButton = itemView.findViewById(R.id.btn_color);
            mOnItemClickListener = onItemClickListener;
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        public void bind(int color) {
            colorButton.setBackgroundColor(mContext.getResources().getColor(color));
        }
    }
}
