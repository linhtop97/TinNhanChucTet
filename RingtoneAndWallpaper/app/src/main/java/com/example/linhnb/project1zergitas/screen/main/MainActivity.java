package com.example.linhnb.project1zergitas.screen.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.ActivityMainBinding;
import com.example.linhnb.project1zergitas.utils.navigator.Navigator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, mainFragment.getClass().getSimpleName());
    }
}
