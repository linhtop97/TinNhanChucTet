package com.example.linhnb.ledbanner;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.linhnb.ledbanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mMainBinding;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        hideNavigationBar();
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Animation animation = initAnimation();
        mMainBinding.textContent.startAnimation(animation);
    }

    private Animation initAnimation() {
        float screenWidth = DimensionUtil.getScreenWidthInPixels(this);
        mMainBinding.textContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 200);
        mMainBinding.textContent.measure(0, 0);

        float x = mMainBinding.textContent.getMeasuredWidth();
        Animation animation = new TranslateAnimation(-x, screenWidth+ 100,
                0, 0);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        animation.setRepeatCount(Animation.INFINITE);

        return animation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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
}
