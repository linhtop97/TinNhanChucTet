package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.screen.base.adapter.IAdapterView;
import com.example.linhnb.project1zergitas.screen.base.adapter.ListAdapter;
import com.example.linhnb.project1zergitas.screen.base.adapter.OnItemClickListener;
import com.example.linhnb.project1zergitas.utils.DimensionUtil;

import java.util.List;

public class WallpaperAdapter extends ListAdapter<String> {

    private int imageWidtth;

    public WallpaperAdapter(Context context, List data) {
        super(context, data);
        imageWidtth = DimensionUtil.setWidthForImageView(mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WallpaperViewHolder wallpaperViewHolder = (WallpaperViewHolder) holder;
        wallpaperViewHolder.bind(mData.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallpaper, parent, false);
        return new WallpaperViewHolder(view, mItemClickListener);
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder implements IAdapterView<String> {

        private ImageView mImageView;
        private OnItemClickListener mOnItemClickListener;

        public WallpaperViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_wallpaper);
            mImageView.getLayoutParams().width = imageWidtth;
            mImageView.getLayoutParams().height = imageWidtth;
            mOnItemClickListener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void bind(String item) {
            if (item == null) {
                return;
            }
            Glide.with(mContext)
                    .load(Uri.parse("file:///android_asset/wallpaper/" + item))
                    .into(mImageView);
        }
    }
}
