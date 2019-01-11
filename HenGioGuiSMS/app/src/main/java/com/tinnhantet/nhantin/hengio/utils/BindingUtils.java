package com.tinnhantet.nhantin.hengio.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:setImageWithGlide"})
    public static void setImageWithGlide(ImageView imageView, int img) {
        Glide.with(imageView.getContext())
                .load(img)
                .into(imageView);
    }
}
