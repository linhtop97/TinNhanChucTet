package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.ActivitySplashBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class SplashActivity extends AppCompatActivity {
    private Navigator mNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mNavigator = new Navigator(this);
        Animation animationIcon = AnimationUtils.loadAnimation(this, R.anim.show_image_splash);
        binding.imgAppIcon.startAnimation(animationIcon);
        Animation animationName = AnimationUtils.loadAnimation(this, R.anim.show_message_welcome);
        binding.txtWelcome.startAnimation(animationName);

        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainApp();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void showMainApp() {
        mNavigator.startActivity(MainActivity.class);
        mNavigator.finishActivity();
    }
}
