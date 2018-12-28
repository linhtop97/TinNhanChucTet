package com.tinnhantet.nhantin.hengio.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.ActivityMainBinding;
import com.tinnhantet.nhantin.hengio.ui.fragments.MainFragment;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        MainFragment fragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, fragment,
                false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }
}
