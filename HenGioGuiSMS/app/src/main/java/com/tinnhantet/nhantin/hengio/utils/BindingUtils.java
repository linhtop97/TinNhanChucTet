package com.tinnhantet.nhantin.hengio.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:setIconCategory"})
    public static void setIconCategory(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }
}
