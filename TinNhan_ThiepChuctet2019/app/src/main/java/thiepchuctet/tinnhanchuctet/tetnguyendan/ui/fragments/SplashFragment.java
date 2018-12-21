package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentSplashBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding mBinding;
    private Navigator mNavigator;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        Glide.with(this)
                .load(R.drawable.ss_splash)
                .into(mBinding.imgBg);
        int SPLASH_DISPLAY_LENGTH = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addMainFragment();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void addMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }
}