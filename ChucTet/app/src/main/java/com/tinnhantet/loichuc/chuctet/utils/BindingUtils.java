package com.tinnhantet.loichuc.chuctet.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:setIconCategory"})
    public static void setIconCategory(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }

    @BindingAdapter({"app:setNumOfMsg"})
    public static void setNumOfMsg(TextView textView, int num) {
        textView.setText(textView.getResources().getString(R.string.num_of_msg).concat(" " + num));
    }

    @BindingAdapter({"app:setBg"})
    public static void setBg(ImageView img, int drawable) {
        Glide.with(MyApplication.getInstance())
                .load(drawable)
                .into(img);
    }
}
