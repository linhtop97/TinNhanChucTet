package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsImpl;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsKey;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.EditMessageFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.MainFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.MessageFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class MainActivity extends AppCompatActivity {

    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        addMainFragment();
    }

    private void addMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }

    public void onSwipeRight() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        if (fragment != null) {
            MessageFragment messageFragment = (MessageFragment) fragment;
            messageFragment.onSwipeLeft();
        }

    }

    public void onSwipeLeft() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        if (fragment != null) {
            MessageFragment messageFragment = (MessageFragment) fragment;
            messageFragment.onSwipeRight();
        }
    }

    public void onSwipeUp() {

    }

    public void onSwipeDown() {
    }

    public void onSingleTab() {
    }

    public void onDoubleTab() {
    }

    public void gotoHomeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        for (int j = 0; j < fm.getBackStackEntryCount(); ++j) {
            fm.popBackStack();
        }
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(EditMessageFragment.class.getSimpleName());
        if (fragmentManager.getBackStackEntryCount() > 1) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            if (fragment != null && EditMessageFragment.class.getSimpleName().equals(fragmentTag)) {
                EditMessageFragment editMessageFragment = (EditMessageFragment) fragment;
                editMessageFragment.confirmLeave(mSharedPrefs.get(SharedPrefsKey.KEY_IS_ADD_NEW, Boolean.class), false);
                return;
            }
        }
        super.onBackPressed();
    }
}
