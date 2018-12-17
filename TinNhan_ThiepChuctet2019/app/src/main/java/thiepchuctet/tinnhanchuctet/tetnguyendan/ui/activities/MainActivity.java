package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.MainFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.MessageFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        Navigator navigator = new Navigator(this);
        MainFragment mainFragment = MainFragment.newInstance();
        navigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
