package com.ledbanner.ledmobile.utils;

import android.databinding.BindingAdapter;
import android.util.TypedValue;
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
            imageButton.setImageResource(R.drawable.ic_play_arrow_active);
        } else {
            imageButton.setImageResource(R.drawable.ic_pause);
        }
    }


    @BindingAdapter({"app:iconRTL"})
    public static void setIconRTL(ImageButton imageButton, boolean isRtl) {
        if (!isRtl) {
            imageButton.setImageResource(R.drawable.ic_scroll_rtl);
        } else {
            imageButton.setImageResource(R.drawable.ic_scroll_rtl_actived);
        }
    }

    @BindingAdapter({"app:iconLTR"})
    public static void setIconLTR(ImageButton imageButton, boolean isRtl) {
        if (!isRtl) {
            imageButton.setImageResource(R.drawable.ic_scroll_ltr_actived);
        } else {
            imageButton.setImageResource(R.drawable.ic_scroll_ltr);
        }
    }

    @BindingAdapter({"app:iconLedorHd"})
    public static void setIconLedOrHd(ImageButton imageButton, boolean isLed) {
        if (!isLed) {
            imageButton.setImageResource(R.drawable.ic_hd);
        } else {
            imageButton.setImageResource(R.drawable.ic_led);
        }
    }

    @BindingAdapter({"app:iconBlinking"})
    public static void setIconBlinking(ImageButton imageButton, boolean isBlinking) {
        if (!isBlinking) {
            imageButton.setImageResource(R.drawable.ic_flash_inactive);
        } else {
            imageButton.setImageResource(R.drawable.ic_flash_active);
        }
    }

    @BindingAdapter({"app:progressTextSpeed"})
    public static void setTextSpeed(SeekBar seekBar, long progress) {
        seekBar.setProgress(4500 - (int) progress);
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
    }
}
