package com.ledbanner.ledmobile.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ledbanner.ledmobile.R;
import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsImpl;
import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsKey;
import com.ledbanner.ledmobile.listeners.OnItemClickListener;

import java.util.List;


public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Integer> mData;
    private Boolean mIsSetTextColor;
    private SharedPrefsImpl mSharedPrefs;
    private int lastSelectedPosition;
    private OnItemClickListener mListener;

    public ColorAdapter(Context context, List<Integer> data, boolean isSetTextColor) {
        mContext = context;
        mData = data;
        mIsSetTextColor = isSetTextColor;
        mSharedPrefs = new SharedPrefsImpl(mContext);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
        if (mIsSetTextColor) {
            if (position == mSharedPrefs.get(SharedPrefsKey.PREF_TEXT_COLOR_POS, Integer.class)) {
                colorViewHolder.checkButton.setBackgroundResource(R.drawable.ic_check);
            } else {
                colorViewHolder.checkButton.setBackgroundResource(0);
            }
        } else {
            if (position == mSharedPrefs.get(SharedPrefsKey.PREF_BG_COLOR_POS, Integer.class)) {
                colorViewHolder.checkButton.setBackgroundResource(R.drawable.ic_check);
            } else {
                colorViewHolder.checkButton.setBackgroundResource(0);
            }
        }
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
        private ImageView checkButton;
        private OnItemClickListener mOnItemClickListener;
        private Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle_color_button);

        public ColorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            colorButton = itemView.findViewById(R.id.btn_color);
            checkButton = itemView.findViewById(R.id.btn_check);
            mOnItemClickListener = onItemClickListener;
            lastSelectedPosition = getAdapterPosition();
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    if (mIsSetTextColor) {
                        mSharedPrefs.put(SharedPrefsKey.PREF_TEXT_COLOR_POS, lastSelectedPosition);
                    } else {
                        mSharedPrefs.put(SharedPrefsKey.PREF_BG_COLOR_POS, lastSelectedPosition);
                    }
                    mOnItemClickListener.onItemClick(lastSelectedPosition);
                    notifyDataSetChanged();

                }
            });
        }

        public void bind(int color) {
            drawable.setColorFilter(mContext.getResources().getColor(color), PorterDuff.Mode.SRC);
            colorButton.setImageDrawable(drawable);
        }
    }
}
