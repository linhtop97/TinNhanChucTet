package com.ledbanner.ledmobile.ui.actvitities;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.ledbanner.ledmobile.R;
import com.ledbanner.ledmobile.databinding.ActivityMainBinding;
import com.ledbanner.ledmobile.utils.DimensionUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        hideNavigationBar();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.textContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 220);
        mBinding.textContent.setText("Hehy");
        mBinding.textContent.setSingleLine(true);
        mBinding.textContent.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mBinding.textContent.setTypeface(mBinding.textContent.getTypeface(), Typeface.BOLD);
        Animation animation = initAnimation();
        mBinding.textContent.startAnimation(animation);
    }

    private Animation initAnimation() {
        float screenWidth = DimensionUtil.getScreenWidthInPixels(this);
        mBinding.textContent.measure(0, 0);
        float x = mBinding.textContent.getMeasuredWidth();
        Animation animation = new TranslateAnimation(-x, screenWidth + 100,
                0, 0);
        animation.setDuration(4000);
        animation.setFillAfter(false);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);

        return animation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    public void hideNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}