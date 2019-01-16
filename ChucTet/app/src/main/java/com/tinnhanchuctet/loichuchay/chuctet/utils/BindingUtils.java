package com.tinnhanchuctet.loichuchay.chuctet.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:setIconCategory"})
    public static void setIconCategory(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }
    @BindingAdapter({"app:setBg"})
    public static void setBg(ImageView img, int drawable) {
        Glide.with(MyApplication.getInstance())
                .load(drawable)
                .into(img);
    }
}
