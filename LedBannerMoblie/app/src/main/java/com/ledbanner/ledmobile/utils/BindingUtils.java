package com.ledbanner.ledmobile.utils;

import android.databinding.BindingAdapter;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ledbanner.ledmobile.R;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:iconPauseOrPlay"})
    public static void setIconPause(ImageButton imageButton, boolean isRunning) {
        if (!isRunning) {
            imageButton.setBackgroundResource(R.drawable.ic_play);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_pause);
        }
    }


    @BindingAdapter({"app:iconRTL"})
    public static void setIconRTL(ImageButton imageButton, boolean isRtl) {
        if (!isRtl) {
            imageButton.setBackgroundResource(R.drawable.ic_rtl);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_rtl_active);
        }
    }

    @BindingAdapter({"app:iconLTR"})
    public static void setIconLTR(ImageButton imageButton, boolean isRtl) {
        if (!isRtl) {
            imageButton.setBackgroundResource(R.drawable.ic_ltr_active);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_ltr);
        }
    }

    @BindingAdapter({"app:iconLedorHd"})
    public static void setIconLedOrHd(ImageButton imageButton, boolean isLed) {
        if (!isLed) {
            imageButton.setBackgroundResource(R.drawable.ic_hd_active);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_led_active);
        }
    }

    @BindingAdapter({"app:iconBlinking"})
    public static void setIconBlinking(ImageButton imageButton, boolean isBlinking) {
        if (!isBlinking) {
            imageButton.setBackgroundResource(R.drawable.ic_flash_off);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_flash_active);
        }
    }

    @BindingAdapter({"app:progressTextSpeed"})
    public static void setTextSpeed(SeekBar seekBar, long progress) {
        seekBar.setProgress(Constans.MAX_DURATION - (int) progress);
    }

    @BindingAdapter({"app:backgroundLed"})
    public static void setBackgroundLed(FrameLayout layout, boolean isLed) {
        if (!isLed) {
            layout.setBackground(null);
        } else {
            layout.setBackground(layout.getResources().getDrawable(R.drawable.led_bg));
        }
    }

    @BindingAdapter({"app:setTextSizeLed"})
    public static void setTextSizeLed(TextView textView, int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        //     textView.setTextSize(textView.getResources().getDimension(R.dimen._1sdp));
    }

    @BindingAdapter({"app:setTextSizeLedMain"})
    public static void setTextSizeLedMain(TextView textView, int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 20);
    }

    @BindingAdapter({"app:setTextColor"})
    public static void setTextColor(TextView textView, int color) {
        textView.setTextColor(textView.getResources().getColor(color));
    }

    @BindingAdapter({"app:setBGColor"})
    public static void setBGColor(ViewGroup layout, int color) {
        layout.setBackgroundColor(layout.getResources().getColor(color));
    }
}
