package com.ledbanner.ledmobile.ui.actvitities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ledbanner.ledmobile.R;
import com.ledbanner.ledmobile.databinding.ActivityMainBinding;
import com.ledbanner.ledmobile.models.TextLed;
import com.ledbanner.ledmobile.utils.Constans;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private TextLed mTextLed;

    public static Intent getMainIntent(Context context, TextLed textLed) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constans.EXTRA_TEXT_LED, textLed);
        intent.putExtra(Constans.BUNDLE_TEXT_LED, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initUI();
    }

    private void initUI() {
        hideNavigationBar();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mTextLed = (TextLed) getIntent().getBundleExtra(Constans.BUNDLE_TEXT_LED).getSerializable(Constans.EXTRA_TEXT_LED);
        mBinding.setTextLed(mTextLed);
        if (mTextLed.isBlinking()) {
            mBinding.textContent.addAnimationBlinking();
            mBinding.textContent.startAnimation(mBinding.textContent.getAnimationSet());
        }
        mBinding.textContent.setRndDuration((int) mTextLed.getTextSpeed());
        if (mTextLed.isRunning()) {
            mBinding.textContent.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            mBinding.textContent.startScroll();
        } else {
            mBinding.textContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mBinding.textContent.pauseScroll();
        }
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

    @Override
    protected void onDestroy() {
        mBinding.textContent.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        super.onDestroy();
    }
}